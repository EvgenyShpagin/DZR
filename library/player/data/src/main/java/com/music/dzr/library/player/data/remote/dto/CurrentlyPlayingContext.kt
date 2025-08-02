package com.music.dzr.library.player.data.remote.dto

import com.music.dzr.core.network.model.track.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the user's currently playing track or episode.
 *
 * @property context The context of the playback.
 * @property timestamp The server-side timestamp in milliseconds.
 * @property progressMs The progress into the currently playing track or episode. Can be `null`.
 * @property isPlaying If something is currently playing.
 * @property item The currently playing track or `null` otherwise.
 * @property currentlyPlayingType The object type of the currently playing item.
 * @property actions Allows to update the user interface based on which playback actions are available within the current context.
 */
@Serializable
data class CurrentlyPlayingContext(
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
