package com.music.dzr.core.network.model

import com.music.dzr.core.network.api.PlayerApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Options for starting or resuming playback.
 * Used as the request body for the [PlayerApi.startOrResumePlayback] endpoint.
 *
 * @property contextUri The URI of the context to play.
 * @property uris A list of Spotify track URIs to play.
 * @property offset Indicates from where in the context playback should start.
 * @property positionMs The position in milliseconds to start playback from.
 */
@Serializable
data class PlaybackOptions(
    @SerialName("context_uri")
    val contextUri: String? = null,
    val uris: List<String>? = null,
    val offset: Offset? = null,
    @SerialName("position_ms")
    val positionMs: Int? = null
)

/**
 * A sealed interface representing where in the context playback should start.
 * Used within [PlaybackOptions].
 */
sealed interface Offset {
    /**
     * Specifies the track to start playback from by its position in the context.
     * @property position The zero-based index of the track.
     */
    @Serializable
    data class ByPosition(val position: Int) : Offset

    /**
     * Specifies the track to start playback from by its URI.
     * @property uri The URI of the track.
     */
    @Serializable
    data class ByUri(val uri: String) : Offset
}
