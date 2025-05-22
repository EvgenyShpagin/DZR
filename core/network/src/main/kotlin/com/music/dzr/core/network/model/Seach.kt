package com.music.dzr.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult<T>(
    val data: List<T>,
    val total: Int,
    @SerialName("next") val nextResultsUrl: String? = null,
    @SerialName("prev") val prevResultsUrl: String? = null
)

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

// Corresponds to album and track
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

@Serializable
data class SearchArtist(
    val id: Int,
    val name: String,
    val link: String,
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

@Serializable
data class SearchPlaylist(
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
    val user: PlaylistCreator,
    val type: String
)

/**
 * Параметры для поиска контента
 */
@Serializable
data class SearchParams(
    val strict: Boolean,
    val order: SearchOrder
)

/**
 * Порядок сортировки результатов поиска
 */
enum class SearchOrder {
    RANKING,
    TRACK_ASC,
    TRACK_DESC,
    ARTIST_ASC,
    ARTIST_DESC,
    ALBUM_ASC,
    ALBUM_DESC,
    RATING_ASC,
    RATING_DESC,
    DURATION_ASC,
    DURATION_DESC
}
