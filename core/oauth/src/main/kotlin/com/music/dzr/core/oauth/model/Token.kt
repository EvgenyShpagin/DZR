package com.music.dzr.core.oauth.model

/**
 * A pure domain model representing an OAuth 2.0 token grant, compliant with RFC 6749.
 *
 * [Successful token response description](https://datatracker.ietf.org/doc/html/rfc6749#section-5.1)
 *
 * This class is independent of any networking or serialization library. It describes the
 * essential components of an access token grant from the application's business logic perspective.
 *
 * @property accessToken The access token issued by the authorization server.
 * @property tokenType The type of the token, typically "Bearer". This determines how the token
 *                     is formatted in the Authorization header.
 * @property expiresInSeconds The lifetime of the access token in seconds.
 * @property refreshToken The token that can be used to obtain a new access token. It can be null,
 *                      as it's not always returned, especially on subsequent token refreshes.
 * @property scopes A list of scopes that have been granted for this token.
 */
data class Token(
    val accessToken: String,
    val tokenType: String,
    val expiresInSeconds: Int,
    val refreshToken: String?,
    val scopes: List<OAuthScope>?
) {
    init {
        require(accessToken.isNotBlank()) { "Access token cannot be blank" }
        require(tokenType.isNotBlank()) { "Token type cannot be blank" }
        require(expiresInSeconds > 0) { "Expires in seconds must be positive" }
    }
}