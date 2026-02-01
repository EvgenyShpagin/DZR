package com.music.dzr.core.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Minimal state-holding navigator with a linear back stack.
 *
 * Exposes [current] destination as observable state for UI.
 * Back stack is kept internally and mutated via navigation methods.
 *
 * Navigation semantics rely on destination equality (`==`):
 * destinations are expected to implement `equals` based on their navigation arguments
 * (for example, by using data classes). This allows the navigator to treat an identical
 * destination as a no-op, while the same destination with different arguments
 * can be handled as an update of the current entry.
 *
 * Note: intended to be used from the main thread (Compose runtime).
 *
 * @param startDestination The initial destination to populate the back stack with.
 */
class Navigator(startDestination: Any) {

    /**
     * The back stack of destinations.
     * The last item is the current destination.
     */
    private val backStack = ArrayDeque<Any>().apply { addLast(startDestination) }

    /**
     * Current destination (top of the back stack).
     * Observed by Compose; updated after any successful navigation operation.
     */
    var current by mutableStateOf(startDestination)
        private set

    /**
     * Navigates to a new [destination].
     *
     * @param destination The target destination.
     * @param updateIfSameChanged If `true`, updates the current destination instead of pushing
     * a new one when navigating within the same destination but with different arguments.
     */
    fun goTo(destination: Any, updateIfSameChanged: Boolean = false) {
        val current = backStack.last()
        if (current == destination) return

        if (updateIfSameChanged && current::class == destination::class) {
            backStack[backStack.lastIndex] = destination
        } else {
            backStack.add(destination)
        }
        updateCurrentDestination()
    }

    /**
     * Navigates back by popping the top destination from the back stack.
     * Does nothing if there is only one destination remaining (the root).
     */
    fun goBack() {
        if (!canGoBack()) return
        backStack.removeAt(backStack.lastIndex)
        updateCurrentDestination()
    }

    /**
     * Checks if it is possible to navigate back.
     *
     * @return `true` if the back stack contains more than one destination.
     */
    fun canGoBack(): Boolean = backStack.size > 1

    /**
     * Pops the back stack until a destination of type [D] is found at the top.
     * If multiple instances exist, it pops back to the most recent one (highest in the stack).
     * If the type [D] is not found, or if it is already at the top, no action is taken.
     */
    inline fun <reified D : Any> goBackTo() {
        goBackTo(cls = D::class.java)
    }

    /**
     * Pops the back stack until a destination of type [D] is found at the top.
     * If multiple instances exist, it pops back to the most recent one (highest in the stack).
     * If the type [D] is not found, or if it is already at the top, no action is taken.
     */
    fun <D : Any> goBackTo(cls: Class<D>) {
        goBackTo { cls.isInstance(it) }
    }

    /**
     * Pops the back stack until the specific [destination] instance is found at the top.
     * If the [destination] is not found, or if it is already at the top, no action is taken.
     */
    fun goBackTo(destination: Any) {
        goBackTo { it == destination }
    }

    /**
     * Pops everything above the last matching destination.
     */
    private inline fun goBackTo(crossinline predicate: (Any) -> Boolean) {
        val index = backStack.indexOfLast(predicate)
        if (index == -1 || index == backStack.lastIndex) return
        backStack.subList(index + 1, backStack.size).clear()
        updateCurrentDestination()
    }

    /**
     * Keeps [current] in sync with the top of the stack.
     */
    private fun updateCurrentDestination() {
        current = backStack.last()
    }

    /**
     * Returns an immutable snapshot of the current back stack (for tests/debug).
     */
    internal fun snapshot(): List<Any> = backStack.toList()
}
