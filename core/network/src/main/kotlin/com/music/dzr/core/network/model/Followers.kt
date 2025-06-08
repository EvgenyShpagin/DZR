package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Followers(
    val href: String?,
    val total: Int
)