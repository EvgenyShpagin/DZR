package com.music.dzr.core.pagination

/**
 * Parameters for requesting a page with cursor-based pagination.
 *
 * @property cursor The position token from which to continue; `null` starts from the beginning.
 */
data class CursorPageable(
    override val limit: Int,
    val cursor: String?
) : Pageable {

    init {
        require(limit > 0) { "The pagination limit must be > 0" }
        require(cursor?.isBlank() != true) { "The pagination cursor must not be empty" }
    }

    companion object {
        val Default = CursorPageable(limit = 20, cursor = null)
    }
}
