package com.music.dzr.core.network.model.browse

import com.music.dzr.core.network.model.shared.PaginatedList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple [BrowseCategory]
 */
@Serializable
data class BrowseCategories(
    @SerialName("categories")
    val items: PaginatedList<BrowseCategory>
)