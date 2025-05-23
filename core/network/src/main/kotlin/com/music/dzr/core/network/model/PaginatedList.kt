package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Generic pagination response class for Deezer API.
 * Contains a list of items, total count, and a link to the next page if available.
 *
 * @param T The type of items contained in the paginated response.
 */
@Serializable
data class PaginatedList<T>(
    val data: List<T>,
    val total: Int? = null,
    @SerialName("next") val nextResultsUrl: String? = null,
    @SerialName("prev") val prevResultsUrl: String? = null
)