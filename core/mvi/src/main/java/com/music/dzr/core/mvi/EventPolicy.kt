package com.music.dzr.core.mvi

/**
 * Defines how a UI event should be routed before it reaches [ImmutableStateViewModel.handleEvent].
 *
 * Policies can be shared between different event types by using the same [ImmutableStateViewModel.groupKey].
 */
sealed interface EventPolicy {
    /**
     * The event is forwarded to the handler queue immediately.
     */
    data object Immediate : EventPolicy

    /**
     * Only the latest event in a group is forwarded after [timeoutMs] of silence.
     */
    data class Debounce(val timeoutMs: Long = 300) : EventPolicy

    /**
     * The first event in a group is forwarded immediately.
     * Next events in the same group are ignored for [windowMs].
     */
    data class ThrottleFirst(val windowMs: Long = 500) : EventPolicy
}
