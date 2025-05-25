package com.music.dzr.core.network.model

/**
 * A permission that app can ask user as part of the authentication process
 */
data class Permission(
    val name: String,
    val title: String,
    val description: String
)