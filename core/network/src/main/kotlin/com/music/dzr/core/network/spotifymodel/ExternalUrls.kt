package com.music.dzr.core.network.spotifymodel

import kotlinx.serialization.Serializable

/**
 * Represents known external URLs for various Spotify objects.
 */
@Serializable
data class ExternalUrls(
    val spotify: String
)