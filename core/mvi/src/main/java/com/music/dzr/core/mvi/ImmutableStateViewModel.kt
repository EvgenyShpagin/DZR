package com.music.dzr.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.cancellation.CancellationException

internal const val DEFAULT_EVENTS_CAPACITY = 16
internal const val DEFAULT_UI_EFFECTS_CAPACITY = 4

/**
 * Base ViewModel for immutable MVI state with centralized event queueing and routing.
 *
 * Event flow:
 * 1. UI sends an event via [onEvent].
 * 2. Event is put into [incomingEvents] in arrival order.
 * 3. Policy router applies [UiEvent.policy].
 * 4. Accepted events are pushed to [routedEvents].
 * 5. [handleEvent] processes [routedEvents] sequentially.
 *
 * This keeps event orchestration in one place and lets feature ViewModels
 * define only event handling logic and routing policy.
 *
 * @param State The type representing the current state of the UI.
 * @param Event The type representing user-driven events.
 * @param Effect The type representing one-time effects.
 * @param eventsCapacity Capacity of the incoming/routing UI events queue.
 * @param uiEffectsCapacity Capacity of the one-time UI effects queue.
 * @param coroutineScope Scope used for internal event routing and handling coroutines.
 * @param elapsedRealtime Clock source for throttle timestamps. Defaults to [android.os.SystemClock.elapsedRealtime].
 */
abstract class ImmutableStateViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    eventsCapacity: Int = DEFAULT_EVENTS_CAPACITY,
    uiEffectsCapacity: Int = DEFAULT_UI_EFFECTS_CAPACITY,
    coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
    private val elapsedRealtime: () -> Long = { android.os.SystemClock.elapsedRealtime() },
) : ViewModel(coroutineScope) {

    init {
        require(eventsCapacity > 0) {
            "eventsCapacity must be > 0, was $eventsCapacity"
        }
        require(uiEffectsCapacity > 0) {
            "uiEffectsCapacity must be > 0, was $uiEffectsCapacity"
        }
    }

    /**
     * Current immutable UI state exposed to the screen layer.
     */
    abstract val uiState: StateFlow<State>

    /**
     * Channel of one-time effects (navigation, snackbar, etc.).
     */
    private val _uiEffects = Channel<Effect>(capacity = uiEffectsCapacity)

    /**
     * One-time effects stream (navigation, snackbar, etc.).
     */
    val uiEffect = _uiEffects.receiveAsFlow()

    /**
     * Entry queue for all events coming from the UI.
     */
    private val incomingEvents = Channel<Event>(capacity = eventsCapacity)

    /**
     * Internal queue for events accepted by routing policies.
     */
    private val routedEvents = Channel<Event>(capacity = eventsCapacity)

    /**
     * Last accepted timestamp per group key for [UiEvent.ThrottleFirst].
     */
    private val throttleLastHandledAt = mutableMapOf<Any, Long>()

    /**
     * Active jobs per group key for [UiEvent.Debounce].
     */
    private val debounceJobs = ConcurrentHashMap<Any, Job>()

    init {
        viewModelScope.launch {
            for (event in incomingEvents) {
                routeEvent(event)
            }
        }

        viewModelScope.launch {
            for (event in routedEvents) {
                try {
                    handleEvent(event)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    onEventHandlingError(event, e)
                }
            }
        }
    }

    /**
     * Accepts a new user event. This is the single public entry point for events from UI.
     */
    fun onEvent(event: Event) {
        incomingEvents.trySend(event)
            .onFailure { exception ->
                onEventDropped(event, exception)
            }
    }

    /**
     * Handles events accepted by routing policy.
     *
     * Uncaught exceptions are caught by the processing loop
     * and forwarded to [onEventHandlingError] without cancelling the pipeline.
     */
    protected abstract suspend fun handleEvent(event: Event)

    /**
     * Emits a one-time effect for UI reaction.
     */
    protected fun triggerEffect(effect: Effect) {
        _uiEffects.trySend(effect)
            .onFailure { exception ->
                onEffectDropped(effect, exception)
            }
    }

    /**
     * Applies policy-specific routing before sending the event to [routedEvents].
     */
    private suspend fun routeEvent(event: Event) {
        when (val policy = event.policy) {
            UiEvent.Immediate -> routedEvents.send(event)
            is UiEvent.ThrottleFirst -> routeWithThrottle(event, policy)
            is UiEvent.Debounce -> routeWithDebounce(event, policy)
        }
    }

    /**
     * Forwards only the first event in a group within [UiEvent.ThrottleFirst.windowMs].
     * Events are grouped by [UiEvent.Scope] if set, otherwise by event class.
     */
    private suspend fun routeWithThrottle(event: Event, policy: UiEvent.ThrottleFirst) {
        val key = policy.scope ?: event::class
        val now = elapsedRealtime()
        val lastHandledAt = throttleLastHandledAt[key] ?: Long.MIN_VALUE

        if (now - lastHandledAt >= policy.windowMs) {
            throttleLastHandledAt[key] = now
            routedEvents.send(event)
        }
    }

    /**
     * Cancels previous pending event in the same group and schedules a new one.
     * Events are grouped by [UiEvent.Scope] if set, otherwise by event class.
     */
    private fun routeWithDebounce(event: Event, policy: UiEvent.Debounce) {
        val key = policy.scope ?: event::class
        debounceJobs.remove(key)?.cancel()
        val job = viewModelScope.launch {
            delay(policy.timeoutMs)
            routedEvents.send(event)
        }
        debounceJobs[key] = job
        job.invokeOnCompletion {
            debounceJobs.remove(key, job)
        }
    }

    /**
     * Called when [onEvent] fails to enqueue [event].
     */
    protected open fun onEventDropped(event: Event, exception: Throwable?) {}

    /**
     * Called when [triggerEffect] fails to enqueue [effect].
     */
    protected open fun onEffectDropped(effect: Effect, exception: Throwable?) {}

    /**
     * Called when [handleEvent] throws an exception.
     * The event processing pipeline remains active after this call.
     */
    protected open fun onEventHandlingError(event: Event, exception: Exception) {}

    /**
     * Cancels internal policy jobs and closes channels to avoid leaks.
     */
    override fun onCleared() {
        debounceJobs.values.forEach(Job::cancel)
        incomingEvents.close()
        routedEvents.close()
        _uiEffects.close()
        throttleLastHandledAt.clear()
        debounceJobs.clear()
        super.onCleared()
    }
}
