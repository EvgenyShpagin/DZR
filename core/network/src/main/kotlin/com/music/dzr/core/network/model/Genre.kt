package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents music genre in Deezer system.
 * Contains genre metadata including images and identifiers.
 */
@Serializable
data class Genre(
    val id: Long,
    val name: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val type: String
)

/**
 * Represents an artist associated with a musical genre in Deezer.
 */
@Serializable
data class GenreArtist(
    val id: Long,
    val name: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val radio: Boolean,
    val tracklist: String,
    val type: String
)