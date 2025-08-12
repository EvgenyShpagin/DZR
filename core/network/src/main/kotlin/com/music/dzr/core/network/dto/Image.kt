package com.music.dzr.core.network.dto

import kotlinx.serialization.Serializable

/**
 * Represents an image object for various Spotify objects.
 */
@Serializable
data class Image(
    val url: String,
    val height: Int?,
    val width: Int?
)