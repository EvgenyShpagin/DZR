package com.music.dzr.core.network.model.player

import com.music.dzr.core.network.model.track.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the user's playback queue.
 *
 * @property currentlyPlaying The currently playing track or episode. Can be `null` if nothing is playing.
 * @property queue The tracks or episodes in the queue.
 */
@Serializable
data class Queue(
    @SerialName("currently_playing")
    val currentlyPlaying: Track?,
    val queue: List<Track>
)