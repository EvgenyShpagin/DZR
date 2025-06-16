package com.music.dzr.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a track saved in the current user's library.
 *
 * @property addedAt The date and time the track was saved.
 * @property track Full information about the track.
 */
@Serializable
data class SavedTrack(
    @SerialName("added_at")
    val addedAt: Instant,
    val track: Track,
) 