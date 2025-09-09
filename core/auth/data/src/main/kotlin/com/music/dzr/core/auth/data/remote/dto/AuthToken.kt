package com.music.dzr.core.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the response from the Spotify token endpoint.
 *
 * @property accessToken The access token to be used for API calls.
 * @property refreshToken A token that can be used to obtain a new access token.
 * @property expiresIn The time period in seconds for which the access token is valid.
 * @property scope A space-separated list of scopes which have been granted.
 * @property tokenType How the access token may be used, always "Bearer".
 */
@Serializable
internal data class AuthToken(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String?,
    @SerialName("expires_in")
    val expiresIn: Int,
    val scope: String?,
    @SerialName("token_type")
    val tokenType: String
)
