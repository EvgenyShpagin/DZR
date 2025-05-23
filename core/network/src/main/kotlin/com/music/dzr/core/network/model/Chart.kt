package com.music.dzr.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Chart(
    val tracks: ChartTracks,
    val albums: ChartAlbums,
    val artists: ChartArtists,
    val playlists: ChartPlaylists
)

typealias ChartTracks = PaginatedList<ChartTrack>

@Serializable
data class ChartTrack(
    val id: Int,
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

@Serializable
data class ChartTrackAlbum(
    val id: Int,
    val title: String,
    val cover: String,
    val coverSmall: String,
    val coverMedium: String,
    val coverBig: String,
    val coverXl: String,
    @SerialName("md5_image") val md5Image: String,
    val tracklist: String,
    val type: String
)

typealias ChartAlbums = PaginatedList<ChartAlbum>

@Serializable
data class ChartAlbum(
    val id: Int,
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

typealias ChartArtists = PaginatedList<ChartArtist>

@Serializable
data class ChartArtist(
    val id: Int,
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

@Serializable
data class ChartArtistBrief(
    val id: Int,
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

typealias ChartPlaylists = PaginatedList<ChartPlaylist>

@Serializable
data class ChartPlaylist(
    val id: Int,
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
    @SerialName("creation_date") val creationDate: Instant,
    @SerialName("add_date") val addDate: Instant,
    @SerialName("mod_date") val modDate: Instant,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("picture_type") val pictureType: String,
    val position: Int,
    val user: PlaylistCreator,
    val type: String
)
