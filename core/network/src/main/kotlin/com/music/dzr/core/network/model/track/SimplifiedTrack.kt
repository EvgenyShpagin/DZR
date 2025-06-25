package com.music.dzr.core.network.model.track

import com.music.dzr.core.network.model.artist.SimplifiedArtist
import com.music.dzr.core.network.model.shared.ExternalUrls
import com.music.dzr.core.network.model.shared.LinkedFrom
import com.music.dzr.core.network.model.shared.Restrictions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents simplified track information returned inside an album’s track list.
 */
@Serializable
data class SimplifiedTrack(
    val artists: List<SimplifiedArtist>,
    @SerialName("available_markets") val availableMarkets: List<String>,
    @SerialName("disc_number") val discNumber: Int,
    @SerialName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @SerialName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    @SerialName("is_playable") val isPlayable: Boolean? = null,
    @SerialName("linked_from") val linkedFrom: LinkedFrom? = null,
    val restrictions: Restrictions? = null,
    val name: String,
    @SerialName("preview_url") val previewUrl: String?,
    @SerialName("track_number") val trackNumber: Int,
    val type: String,
    val uri: String,
    @SerialName("is_local") val isLocal: Boolean
)