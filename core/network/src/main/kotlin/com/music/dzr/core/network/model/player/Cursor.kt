package com.music.dzr.core.network.model.player

import kotlinx.serialization.Serializable

/**
 * Represents a cursor for paging through a list of items.
 *
 * @property after The cursor to use as a key to find the next page of items.
 * @property before The cursor to use as a key to find the previous page of items.
 */
@Serializable
data class Cursor(
    val after: String?,
    val before: String? = null
)