package com.music.dzr.core.mvi

import com.music.dzr.core.mvi.UiEvent.Input

/**
 * A testing implementation of [UiEvent] with various policy configurations (Immediate, Debounce, Throttle)
 * used to verify the event processing pipeline in the MVI architecture.
 */
internal sealed class TestEvent(policy: Policy = Immediate) : UiEvent(policy) {

    data object Click : TestEvent()
    
    data class Input(val text: String) : TestEvent()

    data object ThrottledClick : TestEvent(
        policy = ThrottleFirst(windowMs = WINDOW_MS)
    )
    
    data object NavThrottle : TestEvent(
        policy = ThrottleFirst(windowMs = WINDOW_MS, scope = Navigation)
    )
    
    data object MutationThrottle : TestEvent(
        policy = ThrottleFirst(windowMs = WINDOW_MS, scope = Mutation)
    )

    data class DebouncedSearch(val query: String) : TestEvent(
        policy = Debounce(timeoutMs = TIMEOUT_MS)
    )
    
    data class DebouncedNav(val v: String) : TestEvent(
        policy = Debounce(timeoutMs = TIMEOUT_MS, scope = Navigation)
    )

    data class DebouncedInput(val v: String) : TestEvent(
        policy = Debounce(timeoutMs = TIMEOUT_MS, scope = Input)
    )

    companion object {
        const val WINDOW_MS = 500L
        const val TIMEOUT_MS = 300L
    }
}

