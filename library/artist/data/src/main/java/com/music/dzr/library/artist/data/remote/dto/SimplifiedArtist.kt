package com.music.dzr.library.artist.data.remote.dto

import com.music.dzr.core.network.model.shared.ExternalUrls
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A simplified artist object used in album and track contexts.
 */
@Serializable
data class SimplifiedArtist(
    @SerialName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)