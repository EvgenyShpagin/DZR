package com.music.dzr.core.model.shared

/**
 * Represents a paginated list of items [T].
 */
data class PaginatedList<T>(
    val items: List<T>,
    val total: Int
) {
    /**
     * A convenience property to check if there are more items to load.
     */
    val hasMore: Boolean
        get() = items.size < total
}
