package com.music.dzr.library.user.data.remote.dto

import com.music.dzr.core.network.model.ExternalUrls
import com.music.dzr.core.network.model.Followers
import com.music.dzr.core.network.model.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents current user profile on Spotify, including subscription and content settings.
 *
 * @property country The country of the user, as set in the user's account profile.
 * @property displayName The name displayed on the user's profile.
 * @property email The user's email address.
 * @property explicitContent The user's explicit content settings.
 * @property externalUrls Known external URLs for this user.
 * @property followers Information about the followers of this user.
 * @property href A link to the Web API endpoint for this user.
 * @property id The Spotify user ID for this user.
 * @property images The user's profile image.
 * @property product The user's Spotify subscription level (`null` if [PermissionScope.UserReadPrivate] is not provided).
 * @property type The object type: "user".
 * @property uri The Spotify URI for this user.
 */
@Serializable
data class CurrentUser(
    val country: String,
    @SerialName("display_name")
    val displayName: String,
    val email: String,
    @SerialName("explicit_content")
    val explicitContent: ExplicitContent,
    @SerialName("external_urls")
    val externalUrls: ExternalUrls,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<Image>,
    val product: SubscriptionLevel? = null,
    val type: String,
    val uri: String,
)
