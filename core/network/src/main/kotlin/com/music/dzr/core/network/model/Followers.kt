package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

/**
 * Represents the information about the followers of the artist.
 */
@Serializable
data class Followers(
    val href: String?,
    val total: Int
)