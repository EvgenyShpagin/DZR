package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a snapshot ID for a playlist.
 *
 * @property snapshotId The snapshot ID, which is a unique identifier for a version of a playlist.
 */
@Serializable
data class SnapshotId(
    @SerialName("snapshot_id")
    val snapshotId: String,
)