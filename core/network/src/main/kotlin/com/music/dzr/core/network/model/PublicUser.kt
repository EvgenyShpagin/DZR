package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a public user profile on Spotify.
 *
 * @property id The Spotify user ID for this user.
 * @property displayName The name displayed on the user's profile. `null` if not available.
 * @property externalUrls Known public external URLs for this user.
 * @property followers Information about the followers of this user.
 * @property href A link to the Web API endpoint for this user.
 * @property images The user's profile image.
 * @property type The object type: "user".
 * @property uri The Spotify URI for this user.
 */
@Serializable
data class PublicUser(
    val id: String,
    @SerialName("display_name")
    val displayName: String? = null,
    @SerialName("external_urls")
    val externalUrls: ExternalUrls,
    val followers: Followers? = null,
    val href: String,
    val images: List<Image>? = null,
    val type: String,
    val uri: String,
) 