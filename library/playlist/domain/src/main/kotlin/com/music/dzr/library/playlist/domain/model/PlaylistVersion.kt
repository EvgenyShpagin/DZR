package com.music.dzr.library.playlist.domain.model

/**
 * Represents a playlist version identifier used for optimistic locking
 * to prevent concurrent modification conflicts.
 */
@JvmInline
value class PlaylistVersion(
    private val versionId: String
) {
    override fun toString() = versionId

    companion object
}
