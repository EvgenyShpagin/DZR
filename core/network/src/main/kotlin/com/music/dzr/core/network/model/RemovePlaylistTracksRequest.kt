package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body for removing tracks from a playlist.
 *
 * @property tracks An array of objects containing Spotify URI of the tracks to remove.
 * @property snapshotId The playlist's snapshot ID against which you want to make the changes.
 */
@Serializable
data class RemovePlaylistTracksRequest(
    val tracks: List<TrackToRemove>,
    @SerialName("snapshot_id")
    val snapshotId: String? = null
)

/**
 * Describes a track to be removed from a playlist.
 *
 * @property uri The Spotify URI of the track to remove.
 */
@Serializable
data class TrackToRemove(
    val uri: String
)
