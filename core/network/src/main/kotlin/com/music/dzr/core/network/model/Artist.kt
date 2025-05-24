package com.music.dzr.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents basic artist information for reference purposes.
 * Contains core identifiers and navigation URLs.
 */
@Serializable
data class ArtistBrief(
    val id: Int,
    val name: String,
    val link: String? = null,
    val tracklist: String,
    val type: String
)

/**
 * Represents artist brief information with profile images.
 * Extended version of ArtistBrief with various image size URLs.
 */
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

/**
 * Represents a list of related artists for a given artist.
 * Used for displaying artists similar to the current artist.
 */
typealias RelatedArtistList = PaginatedList<Artist>

/**
 * Contains comprehensive artist information.
 * Includes all available artist metadata including statistics and media.
 * Related and found artists using search have no [share].
 */
@Serializable
data class Artist(
    val id: Int,
    val name: String,
    val link: String,
    val share: String? = null,
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

/**
 * Represents a paginated list of an artist's top tracks.
 * Contains the most popular tracks for a specific artist.
 */
typealias ArtistTopTrackList = PaginatedList<ArtistTopTrack>

/**
 * Represents artist's top track with complete information.
 * Used to display artist's most popular compositions.
 */
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
    val album: AlbumBrief,
    val type: String
)

/**
 * Represents a paginated list of albums by an artist.
 * Used to display all albums released by a specific artist.
 */
typealias ArtistAlbums = PaginatedList<ArtistAlbum>

/**
 * Represents album in artist context.
 * Contains album information emphasizing connection to specific artist.
 */
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
    @SerialName("md5_image") val md5Image: String,
    @SerialName("genre_id") val genreId: Int,
    val fans: Int,
    @SerialName("release_date") val releaseDate: Instant,
    @SerialName("record_type") val recordType: String,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    val tracklist: String,
    val type: String
)

/**
 * Represents simplified album version for radio playlists.
 */
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

/**
 * Represents artist radio track collection.
 * Contains auto-generated playlist based on artist's style.
 * Has no link to track on Deezer ([TrackBrief.link])
 */
typealias ArtistRadio = WholeList<TrackBrief>

/**
 * Network representation of artist playlists
 */
typealias ArtistPlaylists = PaginatedList<ArtistPlaylist>

/**
 * Network representation of artist playlist.
 * Has no [PlaylistBrief.nbTracks]
 */
typealias ArtistPlaylist = PlaylistBrief

/**
 * Represents contributor/co-author of musical work.
 * Contains information about artist's role in track/album creation.
 */
@Serializable
data class Contributor(
    val id: Int,
    val name: String,
    val link: String,
    val share: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_xl") val pictureXl: String,
    val radio: Boolean,
    val tracklist: String,
    val role: String
)