@file:UseSerializers(LocalDateTimeWithSpaceSerializer::class)

package com.music.dzr.core.network.model


import com.music.dzr.core.network.util.LocalDateTimeWithSpaceSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

/**
 * Represents a full playlist in Deezer.
 * Contains detailed metadata about the playlist including tracks, creator, images, and dates.
 */
@Serializable
data class Playlist(
    val id: Long,
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
    @SerialName("creation_date") val creationDate: LocalDateTime,
    @SerialName("add_date") val addDate: LocalDateTime,
    @SerialName("mod_date") val modDate: LocalDateTime,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("picture_type") val pictureType: String,
    val creator: PlaylistCreator,
    val type: String,
    val tracks: PlaylistTracks
)

/**
 * Represents brief information about a playlist.
 * Contains essential metadata used for listing playlists without full track details.
 */
@Serializable
data class PlaylistBrief(
    val id: Long,
    val title: String,
    val public: Boolean,
    @SerialName("nb_tracks") val nbTracks: Int? = null,
    val link: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val checksum: String,
    val tracklist: String,
    @SerialName("creation_date") val creationDate: LocalDateTime,
    @SerialName("add_date") val addDate: LocalDateTime,
    @SerialName("mod_date") val modDate: LocalDateTime,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("picture_type") val pictureType: String,
    val user: PlaylistCreator,
    val type: String
)

/**
 * Represents a paginated list of tracks within a playlist.
 * Used to handle large playlists with pagination support when fetching tracks.
 */
typealias PlaylistTracks = PaginatedList<PlaylistTrack>

/**
 * Represents a single track within a playlist.
 * Contains detailed metadata about the track including artist and album references.
 */
@Serializable
data class PlaylistTrack(
    val id: Long,
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

/**
 * Represents album information related to a playlist track.
 * Contains metadata about the album including cover images and tracklist URL.
 */
@Serializable
data class PlaylistTrackAlbum(
    val id: Long,
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

/**
 * Represents the creator of a playlist.
 */
@Serializable
data class PlaylistCreator(
    @SerialName("id") val userId: Long,
    @SerialName("name") val username: String,
    val tracklist: String,
    val type: String
)