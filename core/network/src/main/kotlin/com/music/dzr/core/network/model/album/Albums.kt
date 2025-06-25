package com.music.dzr.core.network.model.album

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple albums.
 */
@Serializable
data class Albums(
    @SerialName("albums") val items: List<Album>
)