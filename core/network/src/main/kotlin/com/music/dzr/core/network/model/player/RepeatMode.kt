package com.music.dzr.core.network.model.player

import com.music.dzr.core.network.retrofit.UrlParameter
import com.music.dzr.core.network.util.getSerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the repeat mode for the user's playback.
 */
@Serializable
enum class RepeatMode : UrlParameter {
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
    Off;

    override val urlValue get() = getSerializedName()
}