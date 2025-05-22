package com.music.dzr.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    val id: Int,
    val title: String,
    val description: String,
    val duration: Int,
    val public: Boolean,
    @SerialName("is_loved_track") val isLovedTrack: Boolean,
    val collaborative: Boolean,
    @SerialName("nb_tracks") val nbTracks: Int,
    val fans: Int,
    val link: String,
    val share: String,
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
    val creator: PlaylistCreator,
    val type: String,
    val tracks: PlaylistTracks
)

@Serializable
data class PlaylistTracks(
    val data: List<PlaylistTrack>
)

@Serializable
data class PlaylistTrack(
    val id: Int,
    val readable: Boolean,
    val title: String,
    @SerialName("title_short") val titleShort: String,
    @SerialName("title_version") val titleVersion: String,
    val isrc: String,
    val link: String,
    val duration: Int,
    val rank: Int,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerialName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerialName("explicit_content_cover") val explicitContentCover: Int,
    val preview: String,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("time_add") val timeAdd: Long,
    val artist: ArtistBrief,
    val album: PlaylistTrackAlbum,
    val type: String
)

@Serializable
data class PlaylistTrackAlbum(
    val id: Int,
    val title: String,
    val upc: String,
    val cover: String,
    @SerialName("cover_small") val coverSmall: String,
    @SerialName("cover_medium") val coverMedium: String,
    @SerialName("cover_big") val coverBig: String,
    @SerialName("cover_xl") val coverXl: String,
    @SerialName("md5_image") val md5Image: String,
    val tracklist: String,
    val type: String
)