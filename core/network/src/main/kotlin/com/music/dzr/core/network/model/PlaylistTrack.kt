package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * Represents a track in a playlist.
 *
 * @property addedAt The date and time the track was added.
 * @property addedBy The Spotify user who added the track.
 * @property isLocal Whether this track is a local file or not.
 * @property track Information about the track.
 */
@Serializable
data class PlaylistTrack(
    @SerialName("added_at")
    val addedAt: Instant,
    @SerialName("added_by")
    val addedBy: PublicUser,
    @SerialName("is_local")
    val isLocal: Boolean,
    val track: Track,
)