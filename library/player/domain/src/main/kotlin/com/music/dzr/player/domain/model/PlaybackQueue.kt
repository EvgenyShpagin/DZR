package com.music.dzr.player.domain.model

import com.music.dzr.core.model.track.Track

/**
 * Represents the user's playback queue.
 *
 * The queue contains the currently playing track and a list of tracks
 * that will be played next.
 *
 * @property currentlyPlaying The currently playing track. May be `null` if nothing is playing
 * @property upcoming List of tracks that will be played after the current one
 *
 * @see com.music.dzr.library.track.domain.model.Track
 * @see PlaybackState
 */
data class PlaybackQueue(
    val currentlyPlaying: Track?,
    val upcoming: List<Track>
)