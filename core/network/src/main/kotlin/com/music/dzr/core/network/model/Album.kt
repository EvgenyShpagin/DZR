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
    val artist: AlbumArtist,
    val tracks: List<AlbumTrack>
)

@Serializable
data class AlbumArtist(
    val id: Int,
    val name: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
)

@Serializable
data class AlbumTrack(
    val id: Int,
    val readable: Boolean,
    val title: String,
    @SerialName("title_short") val titleShort: String,
    @SerialName("title_version") val titleVersion: String,
    val link: String,
    val duration: Int,
    val rank: Int,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    val preview: String,
    val artist: ArtistBrief,
    val album: AlbumSummary
)

@Serializable
data class AlbumSummary(
    val id: Int,
    val title: String,
    val cover: String,
    @SerialName("cover_small") val coverSmall: String,
    @SerialName("cover_medium") val coverMedium: String,
    @SerialName("cover_big") val coverBig: String,
    @SerialName("cover_xl") val coverXl: String,
)

@Serializable
data class AlbumFallback(
    val id: Int,
    val status: String
)
