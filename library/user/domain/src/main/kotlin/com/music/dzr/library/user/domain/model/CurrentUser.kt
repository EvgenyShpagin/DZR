package com.music.dzr.library.user.domain.model

import com.music.dzr.core.model.shared.Image

/**
 * Represents the current authenticated user's profile with full access to private information.
 *
 * This class contains comprehensive user data including private information such as email,
 * country, and content preferences that are only available for the authenticated user.
 * Unlike a public user profile, this includes subscription details and personalized settings.
 *
 * The explicit content settings control what type of content the user can access based on
 * their preferences and regional restrictions. The subscription level determines available
 * features and limitations.
 */
data class CurrentUser(
    val id: String,
    val country: String,
    val displayName: String,
    val email: String,
    val explicitContentSettings: ExplicitContentSettings,
    val webUrl: String,
    val followerCount: Int,
    val images: List<Image>,
    val subscriptionLevel: SubscriptionLevel
)
