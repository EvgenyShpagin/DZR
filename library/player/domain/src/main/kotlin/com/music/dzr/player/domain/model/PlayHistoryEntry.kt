package com.music.dzr.player.domain.model

import com.music.dzr.core.model.track.Track
import kotlin.time.Instant

/**
 * Represents an entry in the user's playback history.
 *
 * Each entry contains information about a track that was played,
 * when it was played, and the context from which it was played.
 *
 * @property track The track that was played
 * @property playedAt The date and time when the track was played
 * @property context The playback context from which the track was played. May be `null` if context is unknown
 *
 * @see com.music.dzr.library.track.domain.model.Track
 * @see PlaybackContext
 */
data class PlayHistoryEntry(
    val track: Track,
    val playedAt: Instant,
    val context: PlaybackContext?
)