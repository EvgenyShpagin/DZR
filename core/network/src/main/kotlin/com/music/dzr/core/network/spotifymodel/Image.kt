package com.music.dzr.core.network.spotifymodel

import kotlinx.serialization.Serializable

/**
 * Represents an image object for album artwork.
 */
@Serializable
data class Image(
    val url: String,
    val height: Int?,
    val width: Int?
)