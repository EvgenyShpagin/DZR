package com.music.dzr.core.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.music.dzr.core.oauth.model.Token as DomainToken

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
data class Token(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    val scope: String,
    @SerialName("token_type")
    val tokenType: String
)

internal fun Token.toDomain(): DomainToken {
    return DomainToken(
        accessToken = accessToken,
        tokenType = tokenType,
        expiresInSeconds = expiresIn,
        refreshToken = refreshToken,
        scope = scope
    )
}