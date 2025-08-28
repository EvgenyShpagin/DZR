package com.music.dzr.core.pagination

/**
 * Represents an offset-based pagination list of items [T].
 */
data class OffsetPage<T>(
    override val items: List<T>,
    val offset: Int,
    val total: Int
) : Page<T> {
    override val hasMore: Boolean
        get() = offset + items.size < total
}
