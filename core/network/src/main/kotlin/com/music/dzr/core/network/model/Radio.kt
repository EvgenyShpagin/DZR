package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a full radio station in Deezer.
 * Contains detailed metadata including description, share URL, images, and tracklist.
 */
@Serializable
data class Radio(
    val id: Long,
    val title: String,
    val description: String,
    val share: String,
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
 * Represents a brief version of a radio station.
 * Contains essential metadata for listing radios without full description.
 */
@Serializable
data class RadioBrief(
    val id: Long,
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
 * Represents a genre group containing multiple radio.
 */
@Serializable
data class TitledGenreRadio(
    val id: Long,
    val title: String,
    val radios: List<RadioBrief>
)

/**
 * Represents a track played on a radio.
 * Contains detailed track metadata including artist, album, and explicit content flags.
 */
@Serializable
data class RadioTrack(
    val id: Long,
    val readable: Boolean,
    val title: String,
    @SerialName("title_short") val titleShort: String,
    @SerialName("title_version") val titleVersion: String? = null,
    val link: String,
    val duration: Int,
    val rank: Int,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerialName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerialName("explicit_content_cover") val explicitContentCover: Int,
    val preview: String,
    val alternative: TrackBrief? = null,
    @SerialName("md5_image") val md5Image: String,
    val artist: ArtistBriefWithPicture,
    val album: AlbumBrief,
    val type: String
)
