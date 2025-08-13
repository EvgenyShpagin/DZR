package com.music.dzr.library.track.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * Track identifier paired with the timestamp when it was added to the user's library.
 *
 * Used inside the "timestamped_ids" array for the Spotify
 * "Save Tracks for Current User" endpoint to preserve the chronological order
 * of saved tracks.
 *
 * @property id The Spotify track ID.
 * @property addedAt The moment the track was added.
 *
 * @see <a href="https://developer.spotify.com/documentation/web-api/reference/save-tracks-user">Save Tracks for User</a>
 */
@Serializable
internal data class TimestampedId(
    val id: String,
    @SerialName("added_at")
    val addedAt: Instant,
)
