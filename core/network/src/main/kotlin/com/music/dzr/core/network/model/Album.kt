package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: Int,
    val title: String,
    val upc: String,
    val link: String,
    val share: String,
    val cover: String,
    @SerialName("cover_small") val coverSmall: String,
    @SerialName("cover_medium") val coverMedium: String,
    @SerialName("cover_big") val coverBig: String,
    @SerialName("cover_xl") val coverXl: String,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("genre_id") val genreId: Int,
    @SerialName("genres") val genres: GenreList,
    val label: String,
    @SerialName("nb_tracks") val nbTracks: Int,
    val duration: Int,
    val fans: Int,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("record_type") val recordType: String,
    val available: Boolean,
    val alternative: Album? = null,
    val tracklist: String,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerialName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerialName("explicit_content_cover") val explicitContentCover: Int,
    val contributors: List<Contributor>,
    val fallback: AlbumFallback,
    val artist: ArtistBriefWithPic,
    val tracks: AlbumTrackList
)

// Returned from GET ALBUM TRACKS
@Serializable // TODO: similar
data class AlbumTrack(
    val id: Int,
    val readable: Boolean,
    val title: String,
    @SerialName("title_short") val titleShort: String,
    @SerialName("title_version") val titleVersion: String,
    val isrc: String,
    val link: String,
    val duration: Int,
    @SerialName("track_position") val trackPosition: Int,
    @SerialName("disk_number") val diskNumber: Int,
    val rank: Int,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerialName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerialName("explicit_content_cover") val explicitContentCover: Int,
    val preview: String,
    @SerialName("md5_image") val md5Image: String,
    val artist: ArtistBrief,
    val type: String
)

@Serializable
data class AlbumTrackList(
    val data: List<AlbumTrackBrief>
)

@Serializable
data class AlbumTrackBrief(
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
    val artist: ArtistBrief,
    val album: AlbumBrief,
    val type: String
)

@Serializable
data class AlbumBrief(
    val id: Int,
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

@Serializable
data class AlbumFallback(
    val id: Int,
    val status: String
)
