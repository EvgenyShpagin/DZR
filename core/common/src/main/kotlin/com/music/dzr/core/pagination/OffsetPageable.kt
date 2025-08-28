package com.music.dzr.core.pagination

/**
 * Parameters for requesting a page with offset-based pagination.
 *
 * @property offset The 0-based index of the first item in the page within the full ordered set.
 */
data class OffsetPageable(
    override val limit: Int,
    val offset: Int
) : Pageable {

    init {
        require(limit > 0) { "The pagination limit must be > 0" }
        require(offset >= 0) { "The pagination offset must be >= 0" }
    }

    companion object {
        val Default = OffsetPageable(limit = 20, offset = 0)
    }
}
