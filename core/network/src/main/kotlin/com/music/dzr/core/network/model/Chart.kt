package com.music.dzr.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents Deezer's complete music chart structure.
 * Contains rankings for tracks, albums, artists, and playlists.
 */
@Serializable
data class Chart(
    val tracks: ChartTracks,
    val albums: ChartAlbums,
    val artists: ChartArtists,
    val playlists: ChartPlaylists
)

/**
 * Represents a collection of tracks from the chart.
 * Contains a list of the most popular compositions with positions in the rating
 */
typealias ChartTracks = PaginatedList<ChartTrack>

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
 * Represents a collection of albums from the chart.
 * Contains a list of the most popular albums with rating positions.
 */
typealias ChartAlbums = PaginatedList<ChartAlbum>

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
 * Presents a collection of artists from the chart.
 * Contains a list of the most popular artists with ranking positions.
 */
typealias ChartArtists = PaginatedList<ChartArtist>

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

/**
 * Represents a collection of playlists from the chart.
 * Contains a list of the most popular user playlists.
 */
typealias ChartPlaylists = PaginatedList<ChartPlaylist>

/**
 * Represents a chart playlist with position information.
 * Includes the chart position and playlist metadata.
 */
@Serializable
data class ChartPlaylist(
    val id: Long,
    val title: String,
    val public: Boolean,
    @SerialName("nb_tracks") val nbTracks: Int,
    val link: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val checksum: String,
    val tracklist: String,
    @SerialName("creation_date") val creationDate: LocalDate,
    @SerialName("add_date") val addDate: LocalDate,
    @SerialName("mod_date") val modDate: LocalDate,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("picture_type") val pictureType: String,
    val position: Int,
    val user: PlaylistCreator,
    val type: String
)
