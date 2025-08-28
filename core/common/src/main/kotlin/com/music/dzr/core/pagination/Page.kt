package com.music.dzr.core.pagination

/**
 * Represents an immutable slice (page) of items in a paginated data source.
 *
 * This is a minimal read-only contract that both cursor-based and offset-based
 * pagination models implement. Implementations decide how to determine whether
 * more items are available (see [hasMore]).
 *
 * @param T The type of items contained in the page.
 * @property items The list of items for the current page.
 * @property hasMore `true` if more items can be fetched, `false` otherwise.
 */
sealed interface Page<T> {
    val items: List<T>
    val hasMore: Boolean
}