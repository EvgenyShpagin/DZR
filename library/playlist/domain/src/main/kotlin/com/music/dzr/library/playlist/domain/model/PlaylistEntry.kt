package com.music.dzr.library.playlist.domain.model

import com.music.dzr.library.track.domain.model.DetailedTrack
import com.music.dzr.library.user.domain.model.User
import kotlin.time.Instant

/**
 * Represents a track as an entry in a playlist, with additional metadata.
 */
data class PlaylistEntry(
    val addedAt: Instant,
    val addedBy: User,
    val track: DetailedTrack,
    val isLocal: Boolean
)