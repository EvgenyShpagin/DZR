package com.music.dzr.core.network.model.album

import com.music.dzr.core.network.model.artist.SimplifiedArtist
import com.music.dzr.core.network.model.shared.ExternalUrls
import com.music.dzr.core.network.model.shared.Image
import com.music.dzr.core.network.model.shared.Restrictions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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