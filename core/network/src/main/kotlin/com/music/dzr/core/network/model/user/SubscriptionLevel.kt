package com.music.dzr.core.network.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the user's Spotify subscription level.
 *
 * The subscription level [Open] can be considered the same as [Free]
 */
@Serializable
enum class SubscriptionLevel {
    @SerialName("premium")
    Premium,

    @SerialName("free")
    Free,

    @SerialName("open")
    Open
}