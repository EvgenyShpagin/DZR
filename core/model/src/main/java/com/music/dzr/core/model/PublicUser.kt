package com.music.dzr.core.model

data class PublicUser(
    val id: Int,
    val name: String,
    val linkToDeezerProfile: String,
    val pictureUrl: String,
    val countryCode: String,
    val flowUrl: String
)