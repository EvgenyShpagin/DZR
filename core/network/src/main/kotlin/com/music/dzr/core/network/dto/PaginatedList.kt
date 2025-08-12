package com.music.dzr.core.network.dto

import kotlinx.serialization.Serializable

/**
 * Generic pagination response class for Spotify API.
 *
 * @param T The type of items contained in the paginated response.
 */
@Serializable
data class PaginatedList<T>(
    val href: String,
    val items: List<T>,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int
)