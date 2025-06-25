package com.music.dzr.core.network.model.player

import com.music.dzr.core.network.model.track.Track
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an entry in the user's recently played tracks.
 *
 * @property track The track the user listened to.
 * @property playedAt The date and time the track was played.
 * @property context The context the track was played from.
 */
@Serializable
data class PlayHistory(
    val track: Track,
    @SerialName("played_at")
    val playedAt: Instant,
    val context: Context?
)