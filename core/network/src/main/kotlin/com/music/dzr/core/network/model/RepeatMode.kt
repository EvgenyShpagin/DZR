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
    Track,

    /**
     * Repeat the current context (e.g. album, playlist).
     */
    @SerialName("context")
    Context,

    /**
     * Will turn repeat off.
     */
    @SerialName("off")
    Off
}
