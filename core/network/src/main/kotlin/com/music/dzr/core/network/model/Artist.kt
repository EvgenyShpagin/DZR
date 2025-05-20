package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistBrief(
    val id: Int,
    val name: String,
    val link: String? = null,
)