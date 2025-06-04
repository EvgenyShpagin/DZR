package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents a track found in search results.
 * Contains detailed metadata including artist, album, and explicit content flags.
 */
@Serializable
data class SearchTrack(
    val id: Long,
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
    val artist: ArtistBriefWithPicture,
    val album: AlbumBrief,
    val type: String
)

/**
 * Represents an album found in search results.
 * Contains essential album metadata with genre information.
 */
@Serializable
data class SearchAlbum(
    val id: Long,
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
    val artist: ArtistBriefWithPicture,
    val type: String
)

/**
 * Represents a user profile found in search results.
 * Contains public user information and image URLs.
 */
@Serializable
data class SearchUser(
    val id: Long,
    val name: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val tracklist: String,
    val type: String
)
