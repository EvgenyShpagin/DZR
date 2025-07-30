package com.music.dzr.player.domain.model

import com.music.dzr.core.model.AudioContent

/**
 * Represents an advertisement that can be played in the player.
 *
 * Ads are played like regular audio content but usually have restrictions
 * on playback control (e.g., cannot seek or skip).
 *
 * @property id Unique identifier of the advertisement
 *
 * @see AudioContent
 */
data class Advertisement(
    override val id: String
) : AudioContent