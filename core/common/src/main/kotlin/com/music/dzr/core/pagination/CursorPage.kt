package com.music.dzr.core.pagination

/**
 * A cursor-based page of items [T].
 *
 * @property nextCursor A token returned by the data source to continue pagination.
 */
data class CursorPage<T>(
    override val items: List<T>,
    val nextCursor: String?
) : Page<T> {
    override val hasMore: Boolean
        get() = !nextCursor.isNullOrEmpty()
}

/**
 * Creates the next `CursorPageable` when `hasMore` is true;
 * otherwise returns `null` to signal there is no next page.
 */
fun <T> CursorPage<T>.nextPageable(limit: Int): CursorPageable? {
    return if (hasMore) CursorPageable(limit, nextCursor) else null
}