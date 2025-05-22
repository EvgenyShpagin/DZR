package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistCreator(
    @SerialName("id") val userId: Int,
    @SerialName("name") val username: String,
    val tracklist: String,
    val type: String
)