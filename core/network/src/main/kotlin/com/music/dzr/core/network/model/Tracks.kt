package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Wrapper for getting track collection
 */
@Serializable
data class Tracks(
    @SerialName("tracks")
    val list: List<Track>
)