package com.music.dzr.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents a single Deezer editorial section.
 * Editorials are curated collections, such as country or genre highlights, with associated imagery.
 */
@Serializable
data class Editorial(
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
 * Represents a single album from the editorial new releases section.
 * Contains detailed album information as presented in the editorial context.
 */
@Serializable
data class EditorialReleasesAlbum(
    val id: Long,
    val title: String,
    val cover: String,
    @SerialName("cover_small") val coverSmall: String,
    @SerialName("cover_medium") val coverMedium: String,
    @SerialName("cover_big") val coverBig: String,
    @SerialName("cover_xl") val coverXl: String,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("release_date") val releaseDate: LocalDate,
    val tracklist: String,
    val artist: ArtistBrief,
    val type: String
)

/**
 * Represents a single album from an editorial selection.
 * Contains detailed information about the album as chosen for editorial highlights.
 */
@Serializable
data class EditorialSelectionAlbum(
    val id: Long,
    val title: String,
    val cover: String,
    @SerialName("cover_small") val coverSmall: String,
    @SerialName("cover_medium") val coverMedium: String,
    @SerialName("cover_big") val coverBig: String,
    @SerialName("cover_xl") val coverXl: String,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("genre_id") val genreId: Int,
    @SerialName("record_type") val recordType: String,
    val tracklist: String,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    val artist: ArtistBrief,
    val type: String
)