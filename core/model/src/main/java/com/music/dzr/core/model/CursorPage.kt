package com.music.dzr.core.model

/**
 * Represents a cursor-based pagination list of items [T].
 */
data class CursorPage<T>(
    override val items: List<T>,
    val nextCursor: String?
) : Page<T> {
    override val hasMore: Boolean
        get() = !nextCursor.isNullOrEmpty()
}
