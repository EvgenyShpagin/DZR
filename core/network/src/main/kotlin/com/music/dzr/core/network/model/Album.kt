package com.music.dzr.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents complete album information in the Deezer API.
 * Contains all album metadata including tracks, artist, covers, and statistics.
 */
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
    @SerialName("genres") val genres: WholeList<AlbumGenre>,
    val label: String,
    @SerialName("nb_tracks") val nbTracks: Int,
    val duration: Int,
    val fans: Int,
    @SerialName("release_date") val releaseDate: LocalDate,
    @SerialName("record_type") val recordType: String,
    val available: Boolean,
    val alternative: Album? = null,
    val tracklist: String,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerialName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerialName("explicit_content_cover") val explicitContentCover: Int,
    val contributors: List<Contributor>,
    val fallback: AlbumFallback? = null,
    val artist: ArtistBriefWithPicture,
    val tracks: PaginatedList<AlbumTrackBrief>
)

/**
 * Represents complete track information within an album.
 * Contains detailed track metadata including position, duration, and preview.
 */
@Serializable
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


/**
 * Represents concise album track information for list displays.
 * Contains essential metadata without excessive detail.
 */
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

/**
 * Represents brief album information for use in lists and references.
 * Contains basic identifiers and URLs of various cover sizes.
 */
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

/**
 * Provides fallback album information when primary data is unavailable.
 * Used for handling situations when album is temporarily inaccessible.
 */
@Serializable
data class AlbumFallback(
    val id: Int,
    val status: String
)

/**
 * Represents genre information within an album.
 */
@Serializable
data class AlbumGenre(
    val id: Int,
    val name: String,
    val picture: String,
    val type: String
)