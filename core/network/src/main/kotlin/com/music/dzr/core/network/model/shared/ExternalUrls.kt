package com.music.dzr.core.network.model.shared

import kotlinx.serialization.Serializable

/**
 * Represents known external URLs for various Spotify objects.
 */
@Serializable
data class ExternalUrls(
    val spotify: String
)