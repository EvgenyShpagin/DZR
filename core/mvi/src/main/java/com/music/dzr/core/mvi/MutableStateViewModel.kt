package com.music.dzr.core.mvi

import androidx.annotation.CallSuper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * An abstract ViewModel for managing UI state and handling events in the MVI pattern.
 * This ViewModel provides additional functionality for managing mutable state.
 *
 * The [uiState] property is now connected to the mutable [_uiState] property, so inheritor shouldn't override it
 *
 * @param State The type representing the current state of the UI.
 * @param Event The type representing one-time events.
 * @param Effect The type representing one-time effects.
 * @param initialUiState The initial state of the UI.
 */
abstract class MutableStateViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialUiState: State
) : ImmutableStateViewModel<State, Event, Effect>() {

    private val _uiState = MutableStateFlow(initialUiState)

    @get:CallSuper
    override val uiState = _uiState.asStateFlow()

    protected fun updateState(transform: (State) -> State) {
        _uiState.update { transform(it) }
    }
}
