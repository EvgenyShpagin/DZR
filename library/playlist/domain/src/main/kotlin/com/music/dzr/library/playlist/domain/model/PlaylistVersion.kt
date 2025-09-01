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

    companion object {
        /**
         * Sentinel value representing an unspecified/unknown playlist version.
         *
         * This must be mapped to a null or omitted parameter in any external API call.
         */
        val Unspecified = PlaylistVersion("unspecified")
    }
}
