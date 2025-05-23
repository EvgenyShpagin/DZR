package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents paginated search results for tracks.
 * Used when performing track searches via Deezer API with pagination support.
 */
typealias SearchTracks = PaginatedList<SearchTrack>

@Serializable
data class SearchTrack(
    val id: Int,
    val readable: Boolean,
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
    val artist: SearchArtistBrief,
    val album: SearchTrackAlbum,
    val type: String
)

@Serializable
data class SearchArtistBrief(
    val id: Int,
    val name: String,
    val link: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val tracklist: String,
    val type: String
)

typealias SearchArtist = Artist
typealias SearchTrackAlbum = AlbumBrief

@Serializable
data class SearchAlbum(
    val id: Int,
    val title: String,
    val link: String,
    val cover: String,
    @SerialName("cover_small") val coverSmall: String,
    @SerialName("cover_medium") val coverMedium: String,
    @SerialName("cover_big") val coverBig: String,
    @SerialName("cover_xl") val coverXl: String,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("genre_id") val genreId: Int,
    @SerialName("nb_tracks") val nbTracks: Int,
    @SerialName("record_type") val recordType: String,
    val tracklist: String,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    val artist: SearchArtistBrief,
    val type: String
)

/**
 * Represents paginated search results for radio stations.
 * Contains brief radio information matching search query.
 */
typealias SearchRadioList = PaginatedList<RadioBrief>

typealias SearchRadio = RadioBrief

@Serializable
data class SearchUser(
    val id: Int,
    val name: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val tracklist: String,
    val type: String
)

/**
 * Represents a paginated list of playlists matching search.
 * Contains brief info about each playlist (with [PlaylistBrief.nbTracks]).
 */
typealias SearchPlaylists = PaginatedList<PlaylistBrief>