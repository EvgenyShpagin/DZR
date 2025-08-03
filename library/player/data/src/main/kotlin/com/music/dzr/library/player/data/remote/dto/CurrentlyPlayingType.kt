package com.music.dzr.library.player.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The object type of the currently playing item.
 */
@Serializable
enum class CurrentlyPlayingType {
    @SerialName("track")
    Track,

    @SerialName("episode")
    Episode,

    @SerialName("ad")
    Ad,

    @SerialName("unknown")
    Unknown
}