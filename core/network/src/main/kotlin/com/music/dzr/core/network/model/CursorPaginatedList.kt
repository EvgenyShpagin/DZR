package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

/**
 * Represents a cursor-based paginated list of items.
 *
 * @param T The type of the items in the list.
 * @property href A link to the Web API endpoint returning the full result of the request.
 * @property items The requested data.
 * @property limit The maximum number of items in the response (as set in the query or by default).
 * @property next URL to the next page of items. (null if none)
 * @property cursors The cursors used to find the next set of items.
 * @property total The total number of items available to return.
 */
@Serializable
data class CursorPaginatedList<T>(
    val href: String,
    val items: List<T>,
    val limit: Int,
    val next: String?,
    val cursors: Cursor,
    val total: Int? = null
)