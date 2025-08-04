package com.music.dzr.core.auth.domain.util

import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.repository.AuthTokenRepository

/**
 * Retrieves the current access token.
 *
 * @return The access token as a [String], or `null` if no token is available.
 */
suspend fun AuthTokenRepository.getAccessToken() = getToken()?.accessToken

/**
 * Retrieves the current token type (e.g., "Bearer").
 *
 * @return The token type as a [String], or `null` if no token is available.
 */
suspend fun AuthTokenRepository.getTokenType() = getToken()?.tokenType

/**
 * Retrieves the current refresh token.
 *
 * @return The refresh token as a [String], or `null` if no token is available.
 */
suspend fun AuthTokenRepository.getRefreshToken() = getToken()?.refreshToken

/**
 * Retrieves the list of authorization scopes associated with the current authentication state.
 *
 * @return A list of [AuthScope] objects representing the authorized scopes.
 */
suspend fun AuthTokenRepository.getScopes() = getToken()?.scopes

/**
 * Determines if the access token is expired or not.
 * @param safetyBufferSeconds A safety buffer in seconds to treat the token as expired
 *                            a bit earlier to account for network latency.
 * @return `true` if the access token has expired or no token is available, `false` otherwise.
 */
suspend fun AuthTokenRepository.isAccessTokenExpired(
    safetyBufferSeconds: Int = 30
) = getToken()?.isExpired(safetyBufferSeconds = safetyBufferSeconds)