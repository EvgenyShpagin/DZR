package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents Deezer's complete music chart structure.
 * Contains rankings for tracks, albums, artists, and playlists.
 * @param tracks the most popular compositions with positions in the rating
 * @param albums the most popular albums with rating positions.
 * @param artists the most popular artists with ranking positions.
 * @param playlists the most popular playlists (doesn't have [PlaylistBrief.addDate] and [PlaylistBrief.modDate])
 */
@Serializable
data class Chart(
    val tracks: PaginatedList<ChartTrack>,
    val albums: PaginatedList<ChartAlbum>,
    val artists: PaginatedList<ChartArtist>,
    val playlists: PaginatedList<PlaylistBrief>
)

/**
 * Contains chart track collection with ranking positions.
 * Lists most popular tracks with chart positions.
 */
@Serializable
data class ChartTrack(
    val id: Long,
    val title: String,
    @SerialName("title_short") val titleShort: String,
    @SerialName("title_version") val titleVersion: String,
    val link: String,
    val duration: Int,
    val rank: Int,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerialName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerialName("explicit_content_cover") val explicitContentCover: Int,
    val preview: String,
    @SerialName("md5_image") val md5Image: String,
    val position: Int,
    val artist: ChartArtistBrief,
    val album: ChartTrackAlbum,
    val type: String
)

/**
 * Represents the album of a track in the context of a chart.
 * A simplified version of the album for display in track charts.
 */
@Serializable
data class ChartTrackAlbum(
    val id: Long,
    val title: String,
    val cover: String,
    @SerialName("cover_small") val coverSmall: String,
    @SerialName("cover_medium") val coverMedium: String,
    @SerialName("cover_big") val coverBig: String,
    @SerialName("cover_xl") val coverXl: String,
    @SerialName("md5_image") val md5Image: String,
    val tracklist: String,
    val type: String
)

/**
 * Represents a charted album with position information.
 * Includes the chart position and basic metadata for the album.
 */
@Serializable
data class ChartAlbum(
    val id: Long,
    val title: String,
    val link: String,
    val cover: String? = null,
    @SerialName("cover_small") val coverSmall: String,
    @SerialName("cover_medium") val coverMedium: String,
    @SerialName("cover_big") val coverBig: String,
    @SerialName("cover_xl") val coverXl: String,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("record_type") val recordType: String,
    val tracklist: String,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    val position: Int,
    val artist: ChartArtistBrief,
    val type: String
)

/**
 * Represents a chart artist with position information.
 * Includes the chart position and basic artist data.
 */
@Serializable
data class ChartArtist(
    val id: Long,
    val name: String,
    val link: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val radio: Boolean,
    val tracklist: String,
    val position: Int,
    val type: String
)

/**
 * Provides a brief overview of the artist in the context of the chart.
 * A simplified version of the artist for display in track and album charts.
 */
@Serializable
data class ChartArtistBrief(
    val id: Long,
    val name: String,
    val link: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val radio: Boolean,
    val tracklist: String,
    val type: String
)
