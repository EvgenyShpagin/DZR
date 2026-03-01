package com.music.dzr.core.mvi

/**
 * Represents user-driven events in the MVI pattern.
 *
 * These events correspond to user actions or interactions,
 * such as button clicks or input changes. Each implementation
 * defines the actions relevant to the specific screen or feature.
 *
 * @param policy Defines how this event is routed before reaching the handler.
 */
abstract class UiEvent(val policy: Policy = Immediate) {

    /**
     * Defines how this event is routed before reaching the handler.
     */
    sealed interface Policy

    /**
     * The event is forwarded to the handler queue immediately.
     */
    data object Immediate : Policy

    /**
     * Only the latest event in a group is forwarded after [timeoutMs] of silence.
     */
    data class Debounce(
        val timeoutMs: Long = 300,
        val scope: Scope? = null
    ) : Policy

    /**
     * The first event in a group is forwarded immediately.
     * Next events in the same group are ignored for [windowMs].
     */
    data class ThrottleFirst(
        val windowMs: Long = 500,
        val scope: Scope? = null
    ) : Policy

    /**
     * Defines the grouping key for events sharing the same throttle/debounce window.
     * Events with the same [Scope] are treated as belonging to one group.
     */
    sealed interface Scope

    /**
     * All events sharing this scope are treated as one group.
     */
    data object Navigation : Scope

    /**
     * Mutations that should not overlap (like, save, delete).
     */
    data object Mutation : Scope

    /**
     * User text input fields.
     */
    data object Input : Scope

    /**
     * Escape hatch for custom grouping logic.
     */
    data class Custom(val key: String) : Scope
}
