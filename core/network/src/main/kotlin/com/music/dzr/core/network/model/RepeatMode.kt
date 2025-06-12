package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the repeat mode for the user's playback.
 */
@Serializable
enum class RepeatMode {
    /**
     * Repeat the current track.
     */
    @SerialName("track")
    TRACK,

    /**
     * Repeat the current context (e.g. album, playlist).
     */
    @SerialName("context")
    CONTEXT,

    /**
     * Will turn repeat off.
     */
    @SerialName("off")
    OFF
} 