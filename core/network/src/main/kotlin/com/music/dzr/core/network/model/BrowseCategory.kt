package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

/**
 * Represents the browse category object
 */
@Serializable
data class BrowseCategory(
    val href: String,
    val icons: List<Image>,
    val id: String,
    val name: String
)
