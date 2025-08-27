package com.music.dzr.core.pagination

/**
 * Represents a page of items in a paginated data set.
 *
 * @param T the type of items contained in the page.
 * @property items the list of items for the current page.
 * @property hasMore `true` if more items are available to load, `false` otherwise.
 */
sealed interface Page<T> {
    val items: List<T>
    val hasMore: Boolean
}