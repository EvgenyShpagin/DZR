package com.music.dzr.library.artist.data.remote.dto

import com.music.dzr.core.network.dto.AlbumGroup
import com.music.dzr.core.network.dto.AlbumType
import com.music.dzr.core.network.dto.ExternalUrls
import com.music.dzr.core.network.dto.Image
import com.music.dzr.core.network.dto.ReleaseDate
import com.music.dzr.core.network.dto.ReleaseDatePrecision
import com.music.dzr.core.network.dto.Restrictions
import com.music.dzr.core.network.dto.SimplifiedArtist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a simplified album version for artist collection
 */
@Serializable
internal data class ArtistAlbum(
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
    @SerialName("album_group") val albumGroup: AlbumGroup
)