package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents contributor/co-author of musical work.
 * Contains information about artist's role in track/album creation.
 */
@Serializable
data class Contributor(
    val id: Int,
    val name: String,
    val link: String,
    val share: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val radio: Boolean,
    val tracklist: String,
    val role: String
)