package com.music.dzr.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a full track in Deezer.
 * Contains detailed metadata.
 */
@Serializable
data class Track(
    val id: Int,
    val readable: Boolean,
    val title: String,
    @SerialName("title_short") val titleShort: String,
    @SerialName("title_version") val titleVersion: String,
    val isrc: String,
    val link: String,
    val share: String,
    val duration: Int,
    @SerialName("track_position") val trackPosition: Int,
    @SerialName("disk_number") val diskNumber: Int,
    val rank: Int,
    @SerialName("release_date") val releaseDate: LocalDate,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerialName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerialName("explicit_content_cover") val explicitContentCover: Int,
    val preview: String,
    val bpm: Float,
    val gain: Float,
    @SerialName("available_countries") val availableCountries: List<String>,
    val alternative: Track? = null,
    val contributors: List<Contributor>,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("track_token") val trackToken: String,
    val artist: Artist,
    val album: TrackAlbum,
    val type: String
)

/**
 * Network short representation of a track, used by many other models.
 * Contains essential metadata for quick reference without full details.
 */
@Serializable
data class TrackBrief(
    val id: Int,
    val readable: Boolean,
    val title: String,
    @SerialName("title_short") val titleShort: String,
    @SerialName("title_version") val titleVersion: String,
    val link: String? = null,
    val duration: Int,
    val rank: Int,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerialName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerialName("explicit_content_cover") val explicitContentCover: Int,
    val preview: String,
    @SerialName("md5_image") val md5Image: String,
    val artist: ArtistBriefWithPicture,
    val album: AlbumBrief
)

/**
 * Represents album information related to a track.
 * Contains metadata including cover images, release date, and tracklist URL.
 */
@Serializable
data class TrackAlbum(
    val id: Int,
    val title: String,
    val link: String,
    val cover: String,
    @SerialName("cover_small") val coverSmall: String,
    @SerialName("cover_medium") val coverMedium: String,
    @SerialName("cover_big") val coverBig: String,
    @SerialName("cover_xl") val coverXl: String,
    @SerialName("release_date") val releaseDate: LocalDate,
    val tracklist: String,
    val type: String
)