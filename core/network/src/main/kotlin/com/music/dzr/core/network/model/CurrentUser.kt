package com.music.dzr.core.network.model

import com.music.dzr.core.network.model.SubscriptionLevel.Free
import com.music.dzr.core.network.model.SubscriptionLevel.Open
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

/**
 * The user's explicit content settings.
 *
 * @property filterEnabled When true, indicates that explicit content should not be played.
 * @property filterLocked When true, indicates that the explicit content setting is locked and can't be changed by the user.
 */
@Serializable
data class ExplicitContent(
    @SerialName("filter_enabled")
    val filterEnabled: Boolean,
    @SerialName("filter_locked")
    val filterLocked: Boolean
)

/**
 * Represents the user's Spotify subscription level.
 *
 * The subscription level [Open] can be considered the same as [Free]
 */
@Serializable
enum class SubscriptionLevel {
    @SerialName("premium")
    Premium,

    @SerialName("free")
    Free,

    @SerialName("open")
    Open
}
