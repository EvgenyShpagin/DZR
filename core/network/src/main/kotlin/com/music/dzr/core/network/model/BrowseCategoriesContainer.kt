package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple [BrowseCategory]
 */
@Serializable
data class BrowseCategoriesContainer(
    val categories: PaginatedList<BrowseCategory>
)