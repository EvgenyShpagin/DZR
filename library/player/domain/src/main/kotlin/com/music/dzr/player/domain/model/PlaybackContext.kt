package com.music.dzr.player.domain.model

/**
 * Represents the playback context - the source from which music is playing.
 *
 * The context can be an album, playlist, artist page, or other source.
 * It determines where tracks come from for playback and what logic
 * is used to transition to the next track.
 *
 * @property id Unique identifier of the context
 * @property type Type of context (album, playlist, etc.)
 * @property externalUrl External URL to the context (e.g., link to playlist in web interface)
 *
 * @see PlaybackContextType
 * @see PlaybackState
 */
data class PlaybackContext(
    val id: String,
    val type: PlaybackContextType,
    val externalUrl: String
)