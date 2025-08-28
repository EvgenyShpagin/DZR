package com.music.dzr.core.pagination

/**
 * An offset-based page of items [T].
 *
 * @property offset The 0-based `offset` representing the index of the first item in this page
 * within the full ordered set.
 * @property total The total amount of items available in the full set.
 */
data class OffsetPage<T>(
    override val items: List<T>,
    val offset: Int,
    val total: Int
) : Page<T> {
    override val hasMore: Boolean
        get() = offset + items.size < total
}

/**
 * Creates the next `OffsetPageable` when `hasMore` is true;
 * otherwise returns `null` to signal there is no next page.
 */
fun <T> OffsetPage<T>.nextPageable(limit: Int): OffsetPageable? {
    return if (hasMore) OffsetPageable(limit, offset + items.size) else null
}