package com.music.dzr.core.network.model.track

import com.music.dzr.core.network.model.album.AlbumType
import com.music.dzr.core.network.model.album.ReleaseDate
import com.music.dzr.core.network.model.album.ReleaseDatePrecision
import com.music.dzr.core.network.model.artist.SimplifiedArtist
import com.music.dzr.core.network.model.shared.Copyright
import com.music.dzr.core.network.model.shared.ExternalIds
import com.music.dzr.core.network.model.shared.ExternalUrls
import com.music.dzr.core.network.model.shared.Image
import com.music.dzr.core.network.model.shared.Restrictions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an album object with all available fields except for the tracks.
 */
@Serializable
data class TrackAlbum(
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
    val copyrights: List<Copyright>? = null,
    @SerialName("external_ids") val externalIds: ExternalIds? = null,
    val genres: List<String>? = null,
    val label: String? = null,
    val popularity: Int? = null
)
