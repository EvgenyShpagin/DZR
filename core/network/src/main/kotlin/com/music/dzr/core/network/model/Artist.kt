package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistBrief(
    val id: Int,
    val name: String,
    val link: String? = null,
    val tracklist: String,
    val type: String
)

@Serializable
data class ArtistBriefWithPicture(
    val id: Int,
    val name: String,
    val link: String? = null,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val tracklist: String,
    val type: String
)

@Serializable
data class Artist(
    val id: Int,
    val name: String,
    val link: String,
    val share: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    @SerialName("nb_album") val nbAlbum: Int,
    @SerialName("nb_fan") val nbFan: Int,
    val radio: Boolean,
    val tracklist: String,
    val type: String
)

@Serializable
data class ArtistTopTrack(
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
    val contributors: List<Contributor>,
    val artist: ArtistBrief,
    val album: AlbumBrief
)

@Serializable
data class ArtistAlbum(
    val id: Int,
    val title: String,
    val link: String,
    val cover: String,
    @SerialName("cover_small") val coverSmall: String,
    @SerialName("cover_medium") val coverMedium: String,
    @SerialName("cover_big") val coverBig: String,
    @SerialName("cover_xl") val coverXl: String,
    @SerialName("genre_id") val genreId: Int,
    val fans: Int,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("record_type") val recordType: String,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    val tracklist: String,
    val type: String
)

@Serializable
data class ArtistRadioAlbum(
    val id: Int,
    val title: String,
    val cover: String,
    @SerialName("cover_small") val coverSmall: String,
    @SerialName("cover_medium") val coverMedium: String,
    @SerialName("cover_big") val coverBig: String,
    @SerialName("cover_xl") val coverXl: String,
    val tracklist: String,
    val type: String
)


@Serializable
data class ArtistRadio(
    val data: List<ArtistRadioTrack>
)

@Serializable
data class ArtistRadioTrack(
    val id: Int,
    val readable: Boolean,
    val title: String,
    @SerialName("title_short") val titleShort: String,
    @SerialName("title_version") val titleVersion: String,
    val duration: Int,
    val rank: Int,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    val preview: String,
    val artist: ArtistBriefWithPicture,
    val album: AlbumBrief
)

@Serializable
data class ArtistPlaylist(
    val id: Int,
    val title: String,
    val public: Boolean,
    val link: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val checksum: String,
    val user: PlaylistCreator
)