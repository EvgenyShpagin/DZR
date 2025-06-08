package com.music.dzr.core.network.model

import com.music.dzr.core.network.util.AlbumGroupSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a Spotify artist with full profile information
 */
@Serializable
data class Artist(
    @SerialName("external_urls") val externalUrls: ExternalUrls,
    val followers: Followers,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)

/**
 * Response wrapper for getting multiple artists
 */
@Serializable
data class ArtistContainer(
    val artists: List<Artist>
)

/**
 * Represents a simplified album version for artist collection
 */
@Serializable
data class ArtistAlbum(
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
    @SerialName("album_group") val albumGroup: String
)

/**
 * This describes the relationship between the artist and the album.
 */
@Serializable(with = AlbumGroupSerializer::class)
enum class AlbumGroup {
    Album,
    Single,
    Compilation,
    AppearsOn
}
