package com.music.dzr.library.playlist.domain.model

/**
 * Represents a playlist's metadata.
 */
data class PlaylistDetails(
    val name: String,
    val isPublic: Boolean? = null,
    val isCollaborative: Boolean? = null,
    val description: String? = null
)