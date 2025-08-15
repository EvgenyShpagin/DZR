package com.music.dzr.core.model

/**
 * Represents an offset-based pagination list of items [T].
 */
data class OffsetPage<T>(
    override val items: List<T>,
    val total: Int
) : Page<T> {
    override val hasMore: Boolean
        get() = items.size < total
}
