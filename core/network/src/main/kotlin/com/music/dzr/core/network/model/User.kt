package com.music.dzr.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val lastname: String? = null,
    val firstname: String? = null,
    val email: String? = null,
    val status: Int? = null,
    val birthday: Instant? = null,
    @SerialName("inscription_date")
    val inscriptionDate: Instant? = null,
    val gender: String? = null,
    val link: String,
    val picture: String,
    @SerialName("picture_small")
    val pictureSmall: String? = null,
    @SerialName("picture_medium")
    val pictureMedium: String? = null,
    @SerialName("picture_big")
    val pictureBig: String? = null,
    @SerialName("picture_xl")
    val pictureXl: String? = null,
    val country: String,
    @SerialName("lang")
    val language: String? = null,
    @SerialName("is_kid")
    val isKid: Boolean? = null,
    @SerialName("explicit_content_level")
    val explicitContentLevel: String? = null,
    @SerialName("explicit_content_levels_available")
    val explicitContentLevelsAvailable: List<String>? = null,
    val tracklist: String
)