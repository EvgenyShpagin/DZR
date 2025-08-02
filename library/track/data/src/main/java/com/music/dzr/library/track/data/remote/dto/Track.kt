package com.music.dzr.library.track.data.remote.dto

import com.music.dzr.core.network.model.artist.SimplifiedArtist
import com.music.dzr.core.network.model.shared.ExternalIds
import com.music.dzr.core.network.model.shared.ExternalUrls
import com.music.dzr.core.network.model.shared.LinkedFrom
import com.music.dzr.core.network.model.shared.Restrictions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a full track information
 */
@Serializable
data class Track(
    val album: TrackAlbum,
    val artists: List<SimplifiedArtist>,
    @SerialName("available_markets") val availableMarkets: List<String>,
    @SerialName("disc_number") val discNumber: Int,
    @SerialName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @SerialName("external_ids") val externalIds: ExternalIds,
    @SerialName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    @SerialName("is_playable") val isPlayable: Boolean? = null,
    @SerialName("linked_from") val linkedFrom: LinkedFrom? = null,
    val restrictions: Restrictions? = null,
    val name: String,
    val popularity: Int,
    @SerialName("track_number") val trackNumber: Int,
    val type: String,
    val uri: String,
    @SerialName("is_local") val isLocal: Boolean
)
