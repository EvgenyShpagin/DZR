package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PrivateUser(
    val id: Int,
    val name: String,
    val link: String,
    val picture: String,
    val tracklist: String
)