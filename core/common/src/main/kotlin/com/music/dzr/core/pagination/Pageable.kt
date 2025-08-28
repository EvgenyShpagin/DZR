package com.music.dzr.core.pagination

/**
 * Read-only contract for requesting a page from a paginated data source.
 *
 * Concrete strategies (e.g., cursor-based or offset-based) should implement this
 * interface and provide their own parameters in addition to `limit`.
 *
 * @property limit The maximum number of items to return.
 */
sealed interface Pageable {
    val limit: Int
}
