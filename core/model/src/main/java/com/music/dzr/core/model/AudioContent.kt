package com.music.dzr.core.model

/**
 * Represents any audio content that can be played in the player.
 *
 * This interface is implemented by all types of content that can be played
 * as the current item in the player (tracks, ads, podcasts, etc.).
 *
 * @property id Unique identifier of the audio content
 *
 * @see com.music.dzr.library.track.domain.model.Track
 * @see com.music.dzr.player.domain.model.Advertisement
 */
interface AudioContent {
    val id: String

    companion object {
        /**
         * Placeholder object for unknown or undefined audio content.
         * Used when the content type cannot be determined.
         */
        val Unknown = object : AudioContent {
            override val id = "unknown"
        }
    }
}