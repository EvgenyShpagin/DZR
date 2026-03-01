package com.music.dzr.core.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * An abstract ViewModel for managing UI state and handling events in the MVI pattern.
 * This ViewModel provides additional functionality for managing mutable state.
 *
 * @param State The type representing the current state of the UI.
 * @param Event The type representing user-driven events.
 * @param Effect The type representing one-time effects.
 * @param initialUiState The initial state of the UI.
 * @param eventsCapacity Capacity of the incoming/routing UI events queue.
 * @param uiEffectsCapacity Capacity of the one-time UI effects queue.
 * @param coroutineScope Scope used for internal event routing and handling coroutines.
 * @param elapsedRealtime Clock source for throttle timestamps. Defaults to [android.os.SystemClock.elapsedRealtime].
 */
abstract class MutableStateViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialUiState: State,
    eventsCapacity: Int = DEFAULT_EVENTS_CAPACITY,
    uiEffectsCapacity: Int = DEFAULT_UI_EFFECTS_CAPACITY,
    coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
    elapsedRealtime: () -> Long = { android.os.SystemClock.elapsedRealtime() },
) : ImmutableStateViewModel<State, Event, Effect>(
    eventsCapacity = eventsCapacity,
    uiEffectsCapacity = uiEffectsCapacity,
    coroutineScope = coroutineScope,
    elapsedRealtime = elapsedRealtime
) {

    private val _uiState = MutableStateFlow(initialUiState)

    final override val uiState = _uiState.asStateFlow()

    /**
     * Atomically updates the current [uiState] using the given [transform].
     */
    protected fun updateState(transform: (State) -> State) {
        _uiState.update { transform(it) }
    }
}
