package com.music.dzr.core.network.model.album

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a saved album in the user's library, with timestamp.
 */
@Serializable
data class SavedAlbum(
    @SerialName("added_at") val addedAt: Instant,
    val album: Album
)