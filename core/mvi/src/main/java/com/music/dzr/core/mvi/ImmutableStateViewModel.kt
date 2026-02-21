package com.music.dzr.core.mvi

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel for immutable MVI state with centralized event queueing and routing.
 *
 * Event flow:
 * 1. UI sends an event via [onEvent].
 * 2. Event is put into [incomingEvents] in arrival order.
 * 3. Policy router applies [policy] and [groupKey] of the event.
 * 4. Accepted events are pushed to [readyEvents].
 * 5. [handleEvent] processes [readyEvents] sequentially.
 *
 * This keeps event orchestration in one place and lets feature ViewModels
 * define only event handling logic and routing policy.
 *
 * @param State The type representing the current state of the UI.
 * @param Event The type representing user-driven events.
 * @param Effect The type representing one-time effects.
 */
abstract class ImmutableStateViewModel<
        State : UiState,
        Event : UiEvent,
        Effect : UiEffect> : ViewModel() {

    /**
     * Current immutable UI state exposed to the screen layer.
     */
    abstract val uiState: StateFlow<State>

    /**
     * One-time effects stream (navigation, snackbar, etc.).
     */
    private val _uiEffects = Channel<Effect>(capacity = Channel.BUFFERED)
    val uiEffect = _uiEffects.receiveAsFlow()

    /**
     * Entry queue for all events coming from the UI.
     */
    private val incomingEvents = Channel<Event>(capacity = Channel.BUFFERED)

    /**
     * Internal queue for events accepted by routing policies.
     */
    private val readyEvents = Channel<Event>(capacity = Channel.BUFFERED)

    /**
     * Last accepted timestamp per group key for [EventPolicy.ThrottleFirst].
     */
    private val throttleLastHandledAt = mutableMapOf<Any, Long>()

    /**
     * Active jobs per group key for [EventPolicy.Debounce].
     */
    private val debounceJobs = mutableMapOf<Any, Job>()

    init {
        // Policy routing stage.
        viewModelScope.launch {
            for (event in incomingEvents) {
                routeEvent(event)
            }
        }

        // Strictly sequential business handling stage.
        viewModelScope.launch {
            for (event in readyEvents) {
                handleEvent(event)
            }
        }
    }

    /**
     * Accepts a new user event. This is the single public entry point for events from UI.
     */
    fun onEvent(event: Event) {
        incomingEvents.trySend(event)
    }

    /**
     * Handles events that have already passed routing policy checks.
     */
    protected abstract suspend fun handleEvent(event: Event)

    /**
     * Per-event routing policy. Override in feature ViewModels if needed.
     */
    protected open val Event.policy: EventPolicy get() = EventPolicy.Immediate

    /**
     * Group key for policy sharing between different event types.
     *
     * Example: "NavigateBack" and "NavigateForward" can return the same key to
     * share one throttle window.
     */
    protected open val Event.groupKey: Any get() = this::class

    /**
     * Emits a one-time effect for UI reaction.
     */
    protected fun triggerEffect(effect: Effect) {
        _uiEffects.trySend(effect)
    }

    /**
     * Applies policy-specific routing before sending the event to [readyEvents].
     */
    private suspend fun routeEvent(event: Event) {
        when (val policy = event.policy) {
            EventPolicy.Immediate -> {
                readyEvents.send(event)
            }

            is EventPolicy.ThrottleFirst -> {
                routeWithThrottle(event, policy)
            }

            is EventPolicy.Debounce -> {
                routeWithDebounce(event, policy)
            }
        }
    }

    /**
     * Forwards only the first event in [groupKey] within [EventPolicy.ThrottleFirst.windowMs].
     */
    private suspend fun routeWithThrottle(event: Event, policy: EventPolicy.ThrottleFirst) {
        val key = event.groupKey
        val now = SystemClock.elapsedRealtime()
        val lastHandledAt = throttleLastHandledAt[key] ?: Long.MIN_VALUE

        if (now - lastHandledAt >= policy.windowMs) {
            throttleLastHandledAt[key] = now
            readyEvents.send(event)
        }
    }

    /**
     * Cancels previous pending event in the same group and schedules a new one.
     */
    private fun routeWithDebounce(event: Event, policy: EventPolicy.Debounce) {
        val key = event.groupKey
        debounceJobs.remove(key)?.cancel()
        debounceJobs[key] = viewModelScope.launch {
            delay(policy.timeoutMs)
            readyEvents.send(event)
        }
    }

    /**
     * Cancels internal policy jobs and closes channels to avoid leaks.
     */
    override fun onCleared() {
        debounceJobs.values.forEach(Job::cancel)
        incomingEvents.close()
        readyEvents.close()
        _uiEffects.close()
        super.onCleared()
    }
}
