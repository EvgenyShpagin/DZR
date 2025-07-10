package com.music.dzr.core.model.shared

/**
 * Represents any audio content that can be played in the player.
 *
 * This interface is implemented by all types of content that can be played
 * as the current item in the player (tracks, ads, podcasts, etc.).
 *
 * @property id Unique identifier of the audio content
 *
 * @see com.music.dzr.core.model.track.Track
 * @see com.music.dzr.core.model.player.Advertisement
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