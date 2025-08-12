package com.music.dzr.library.album.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * Represents a saved album in the user's library, with timestamp.
 */
@Serializable
internal data class SavedAlbum(
    @SerialName("added_at") val addedAt: Instant,
    val album: Album
)