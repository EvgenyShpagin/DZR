package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the full playback state of a user, including the currently playing track, device, and more.
 *
 * @property device The device that is currently active.
 * @property repeatState The repeat state. Can be "track", "context" or "off".
 * @property shuffleState If shuffle is on or off.
 * @property context The context of the playback.
 * @property timestamp The server-side timestamp in milliseconds.
 * @property progressMs The progress into the currently playing track or episode. Can be `null`.
 * @property isPlaying If something is currently playing.
 * @property item The currently playing track or episode. Can be `null`.
 * @property currentlyPlayingType The object type of the currently playing item.
 * @property actions Allows to update the user interface based on which playback actions are available within the current context.
 */
@Serializable
data class PlaybackState(
    val device: Device,
    @SerialName("repeat_state")
    val repeatState: String,
    @SerialName("shuffle_state")
    val shuffleState: Boolean,
    val context: Context?,
    val timestamp: Long,
    @SerialName("progress_ms")
    val progressMs: Int?,
    @SerialName("is_playing")
    val isPlaying: Boolean,
    val item: Track?,
    @SerialName("currently_playing_type")
    val currentlyPlayingType: CurrentlyPlayingType,
    val actions: Actions
)

/**
 * The object type of the currently playing item.
 */
@Serializable
enum class CurrentlyPlayingType {
    @SerialName("track")
    Track,

    @SerialName("episode")
    Episode,

    @SerialName("ad")
    Ad,

    @SerialName("unknown")
    Unknown
}
