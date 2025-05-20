package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Int,
    val name: String,
    val picture: String,
    @SerialName("picture_small") val pictureSmall: String? = null,
    @SerialName("picture_medium") val pictureMedium: String? = null,
    @SerialName("picture_big") val pictureBig: String? = null,
    @SerialName("picture_xl") val pictureXl: String? = null
)

@Serializable
data class GenreList(
    val data: List<Genre>
)