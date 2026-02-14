package com.music.dzr.core.navigation

/**
 * Pops back to the most recent destination of type [D] and updates it with [destination].
 *
 * Useful for `return` + `update` flows
 * (e.g. go back to a previous destination and refresh its arguments) without growing the back stack.
 *
 * If there is no destination of type [D] in the back stack,
 * nothing is popped and [destination] is pushed,
 * replacing the current destination if it has the same runtime type.
 *
 * Note: this relies on [Navigator] destination equality semantics (`==`) and kind matching.
 */
inline fun <reified D : Any> Navigator.goBackToWithUpdate(destination: D) {
    goBackTo<D>()
    goTo(destination, updateIfSameChanged = true)
}
