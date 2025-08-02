package com.music.dzr.library.player.data.remote.dto

import com.music.dzr.core.network.model.shared.ExternalUrls
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the context of the playback, such as a playlist or album.
 *
 * @property type The type of the context, e.g., "album", "playlist".
 * @property href A link to the Web API endpoint providing full details of the context.
 * @property externalUrls External URLs for this context.
 * @property uri The Spotify URI for the context.
 */
@Serializable
data class Context(
    val type: String,
    val href: String,
    @SerialName("external_urls")
    val externalUrls: ExternalUrls,
    val uri: String
)