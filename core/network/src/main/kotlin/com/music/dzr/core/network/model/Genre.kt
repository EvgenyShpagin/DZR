package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents music genre in Deezer system.
 * Contains genre metadata including images and identifiers.
 */
@Serializable
data class Genre(
    val id: Int,
    val name: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val type: String
)

/**
 * Represents a non-paginated list of musical genres.
 * Used to retrieve the full list of available genres in Deezer.
 */
typealias GenreList = WholeList<Genre>

/**
 * Represents a non-paginated list of artists associated with a specific genre.
 * Used to fetch all artists categorized under a given genre.
 */
typealias GenreArtistList = WholeList<GenreArtist>

/**
 * Represents a non-paginated list of radios related to a specific genre.
 * Used to obtain radio stations that broadcast music of the specified genre.
 */
typealias GenreRadioList = WholeList<RadioBrief>

/**
 * Represents an artist associated with a musical genre in Deezer.
 */
@Serializable
data class GenreArtist(
    val id: Int,
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