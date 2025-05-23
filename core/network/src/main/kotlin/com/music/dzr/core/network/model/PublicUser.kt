package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Contains part of Deezer user profile data.
 */
@Serializable
data class PublicUser(
    val id: Int,
    val name: String,
    val link: String,
    val picture: String,
    @SerialName("picture_small")
    val pictureSmall: String,
    @SerialName("picture_medium")
    val pictureMedium: String,
    @SerialName("picture_big")
    val pictureBig: String,
    @SerialName("picture_xl")
    val pictureXl: String,
    val country: String,
    val tracklist: String
)