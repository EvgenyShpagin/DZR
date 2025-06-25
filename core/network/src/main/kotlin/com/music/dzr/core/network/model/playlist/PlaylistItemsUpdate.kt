package com.music.dzr.core.network.model.playlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body for updating items in a playlist.
 * This can be used to reorder or replace items.
 *
 * @property uris A JSON array of the Spotify URIs to set.
 * @property rangeStart The position of the first item to be reordered.
 * @property insertBefore The position where the items should be inserted.
 * @property rangeLength The amount of items to be reordered. Defaults to 1 if not set.
 * @property snapshotId The playlist's snapshot ID against which you want to make the changes.
 */
@Serializable
data class PlaylistItemsUpdate(
    val uris: List<String>? = null,
    @SerialName("range_start")
    val rangeStart: Int? = null,
    @SerialName("insert_before")
    val insertBefore: Int? = null,
    @SerialName("range_length")
    val rangeLength: Int? = null,
    @SerialName("snapshot_id")
    val snapshotId: String? = null,
)