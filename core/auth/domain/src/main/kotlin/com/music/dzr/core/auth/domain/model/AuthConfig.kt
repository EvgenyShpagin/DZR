package com.music.dzr.core.auth.domain.model

/**
 * Configuration values required by the auth module.
 *
 * Provided by the application layer (app module) via DI and consumed by core:auth.
 * Contains OAuth client settings used to build authorization URLs and exchange/refresh tokens.
 *
 * @property clientId OAuth client identifier issued by the provider (e.g., Spotify).
 * @property redirectUri Redirect URI registered for this client and used in OAuth flows.
 */
data class AuthConfig(
    val clientId: String,
    val redirectUri: String
)
