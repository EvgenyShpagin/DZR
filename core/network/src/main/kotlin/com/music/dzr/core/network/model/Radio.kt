package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a full radio station in Deezer.
 * Contains detailed metadata including description, share URL, images, and tracklist.
 */
@Serializable
data class Radio(
    val id: Int,
    val title: String,
    val description: String,
    val share: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    @SerialName("md5_image") val md5Image: String,
    val tracklist: String,
    val type: String
)

/**
 * Represents a brief version of a radio station.
 * Contains essential metadata for listing radios without full description.
 */
@Serializable
data class RadioBrief(
    val id: Int,
    val title: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val tracklist: String,
    @SerialName("md5_image") val md5Image: String,
    val type: String
)

/**
 * Represents a list of genre-titled radio collections.
 * Differs from [GenreRadioList] in that it contains id and title of a genre.
 */
typealias TitledGenreRadioList = WholeList<TitledGenreRadio>

/**
 * Represents a genre group containing multiple radio.
 */
@Serializable
data class RadioByGenre(
    val id: Int,
    val title: String,
    val radios: List<RadioBrief>
)

@Serializable
data class RadioTopTracks(
    val data: List<RadioTrack>
)

/**
 * Represents a track played on a radio.
 * Contains detailed track metadata including artist, album, and explicit content flags.
 */
@Serializable
data class RadioTrack(
    val id: Int,
    val readable: Boolean,
    val title: String,
    @SerialName("title_short") val titleShort: String,
    @SerialName("title_version") val titleVersion: String,
    val link: String,
    val duration: Int,
    val rank: Int,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerialName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerialName("explicit_content_cover") val explicitContentCover: Int,
    val preview: String,
    val alternative: TrackBrief?,
    @SerialName("md5_image") val md5Image: String,
    val artist: ArtistBriefWithPicture,
    val album: AlbumBrief,
    val type: String
)
