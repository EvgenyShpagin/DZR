package com.music.dzr.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a full album object with all available fields.
 */
@Serializable
data class Album(
    @SerialName("album_type") val albumType: AlbumType,
    @SerialName("total_tracks") val totalTracks: Int,
    @SerialName("available_markets") val availableMarkets: List<String>,
    @SerialName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    @SerialName("release_date") val releaseDate: ReleaseDate,
    @SerialName("release_date_precision") val releaseDatePrecision: ReleaseDatePrecision,
    val restrictions: Restrictions? = null,
    val type: String,
    val uri: String,
    val artists: List<SimplifiedArtist>,
    val tracks: PaginatedList<SimplifiedTrack>,
    val copyrights: List<Copyright>? = null,
    @SerialName("external_ids") val externalIds: ExternalIds? = null,
    val genres: List<String>? = null,
    val label: String? = null,
    val popularity: Int? = null
)

/**
 * Represents a saved album in the user’s library, with timestamp.
 */
@Serializable
data class SavedAlbum(
    @SerialName("added_at") val addedAt: Instant,
    val album: Album
)

/**
 * Represents a simplified album in the "new releases" context.
 */
@Serializable
data class SimplifiedAlbum(
    @SerialName("album_type") val albumType: AlbumType,
    @SerialName("total_tracks") val totalTracks: Int,
    @SerialName("available_markets") val availableMarkets: List<String>,
    @SerialName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    @SerialName("release_date") val releaseDate: ReleaseDate,
    @SerialName("release_date_precision") val releaseDatePrecision: ReleaseDatePrecision,
    val restrictions: Restrictions? = null,
    val type: String,
    val uri: String,
    val artists: List<SimplifiedArtist>
)

/**
 * Paging object for new releases.
 */
@Serializable
data class NewReleasesResponse(
    val albums: PaginatedList<SimplifiedAlbum>
)

/**
 * Response wrapper for getting multiple albums.
 */
@Serializable
data class MultipleAlbumsResponse(
    val albums: List<Album>
)

enum class AlbumType {
    Album,
    Single,
    Compilation
}
