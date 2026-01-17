package com.music.dzr.core.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * An abstract ViewModel for managing UI state and handling events in the MVI pattern.
 *
 * @param State The type representing the current state of the UI.
 * @param Event The type representing one-time events.
 * @param Effect The type representing one-time effects.
 */
abstract class ImmutableStateViewModel<
        State : UiState,
        Event : UiEvent,
        Effect : UiEffect> : ViewModel() {

    abstract val uiState: StateFlow<State>

    private val _uiEffects = Channel<Effect>(capacity = Channel.CONFLATED)
    val uiEffect = _uiEffects.receiveAsFlow()

    abstract fun onEvent(event: Event)

    protected fun triggerEffect(effect: Effect) {
        _uiEffects.trySend(effect)
    }
}
