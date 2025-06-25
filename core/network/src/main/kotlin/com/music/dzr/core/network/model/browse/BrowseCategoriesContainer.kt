package com.music.dzr.core.network.model.browse

import com.music.dzr.core.network.model.shared.PaginatedList
import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple [BrowseCategory]
 */
@Serializable
data class BrowseCategoriesContainer(
    val categories: PaginatedList<BrowseCategory>
)