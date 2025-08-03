package com.music.dzr.library.album.data.remote.dto

import com.music.dzr.core.network.model.SimplifiedArtist
import com.music.dzr.core.network.model.Copyright
import com.music.dzr.core.network.model.ExternalIds
import com.music.dzr.core.network.model.ExternalUrls
import com.music.dzr.core.network.model.Image
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.Restrictions
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
    val tracks: PaginatedList<AlbumTrack>,
    val copyrights: List<Copyright>,
    @SerialName("external_ids") val externalIds: ExternalIds,
    val label: String,
    val popularity: Int
)