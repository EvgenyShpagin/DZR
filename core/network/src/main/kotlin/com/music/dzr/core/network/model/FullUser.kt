package com.music.dzr.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FullUser(
    val id: Int,
    val name: String,
    val lastname: String,
    val firstname: String,
    val email: String,
    val status: Int,
    val birthday: Instant,
    @SerialName("inscription_date")
    val inscriptionDate: Instant,
    val gender: String,
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
    @SerialName("lang")
    val language: String,
    @SerialName("is_kid")
    val isKid: Boolean,
    @SerialName("explicit_content_level")
    val explicitContentLevel: String,
    @SerialName("explicit_content_levels_available")
    val explicitContentLevelsAvailable: List<String>,
    val tracklist: String
)
