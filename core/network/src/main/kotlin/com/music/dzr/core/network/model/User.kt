package com.music.dzr.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Contains complete Deezer user profile data.
 * Includes both private and public user profile information.
 */
@Serializable
data class CurrentUser(
    val id: Long,
    val name: String,
    val lastname: String,
    val firstname: String,
    val email: String,
    val status: Int,
    val birthday: LocalDate,
    @SerialName("inscription_date")
    val inscriptionDate: LocalDate,
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
    val tracklist: String,
    val type: String
)

/**
 * Contains part of Deezer user profile data.
 */
@Serializable
data class User(
    val id: Long,
    val name: String,
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
    val country: String? = null,
    val tracklist: String,
    val type: String
)