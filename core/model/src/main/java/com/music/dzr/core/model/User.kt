package com.music.dzr.core.model

/**
 * Represents a public user profile with limited information.
 *
 * This class contains only publicly available user information such as display name,
 * profile images, and follower count. Private information like email, country, and
 * subscription details are not included as they are not accessible for other users.
 */
data class User(
    val id: String,
    val displayName: String?,
    val followerCount: Int,
    val images: List<Image>,
    val webUrl: String
)
