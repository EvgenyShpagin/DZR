package com.music.dzr.core.auth.domain.repository

import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.model.AuthToken
import com.music.dzr.core.auth.domain.model.isExpired
import com.music.dzr.core.error.AppError
import com.music.dzr.core.result.Result
import com.music.dzr.core.result.isSuccess

/**
 * Retrieves the current access token.
 *
 * @return [Result] containing the access token as [String] on success,
 * or one of these errors on failure:
 * - [com.music.dzr.core.auth.domain.error.AuthError],
 * - [com.music.dzr.core.error.ConnectivityError],
 * - [com.music.dzr.core.error.NetworkError]
 */
suspend fun AuthTokenRepository.getAccessToken() = getToken().mapSuccess { it.accessToken }

/**
 * Retrieves the current token type (e.g., "Bearer").
 *
 * @return [Result] containing the token type as [String] on success,
 * or one of these errors on failure:
 * - [com.music.dzr.core.auth.domain.error.AuthError],
 * - [com.music.dzr.core.error.ConnectivityError],
 * - [com.music.dzr.core.error.NetworkError]
 */
suspend fun AuthTokenRepository.getTokenType() = getToken().mapSuccess { it.tokenType }

/**
 * Retrieves the current refresh token.
 *
 * @return [Result] containing the refresh token as [String]? on success. Note that the
 * refresh token may legitimately be `null` in a successful result when the provider
 * does not return it (e.g., on subsequent refreshes). On failure, returns one of:
 * - [com.music.dzr.core.auth.domain.error.AuthError],
 * - [com.music.dzr.core.error.ConnectivityError],
 * - [com.music.dzr.core.error.NetworkError]
 */
suspend fun AuthTokenRepository.getRefreshToken() = getToken().mapSuccess { it.refreshToken }

/**
 * Retrieves the list of authorization scopes associated with the current authentication state.
 *
 * @return [Result] containing a list of [AuthScope] representing the authorized scopes on success,
 * or one of these errors on failure:
 * - [com.music.dzr.core.auth.domain.error.AuthError],
 * - [com.music.dzr.core.error.ConnectivityError],
 * - [com.music.dzr.core.error.NetworkError]
 */
suspend fun AuthTokenRepository.getScopes() = getToken().mapSuccess { it.scopes }

/**
 * Determines if the access token is expired or not.
 * @param safetyBufferSeconds A safety buffer in seconds to treat the token as expired
 *                            a bit earlier to account for network latency.
 * @return [Result] containing `true` if the access token has expired, `false` otherwise,
 * or one of these errors on failure:
 * - [com.music.dzr.core.auth.domain.error.AuthError],
 * - [com.music.dzr.core.error.ConnectivityError],
 * - [com.music.dzr.core.error.NetworkError]
 */
suspend fun AuthTokenRepository.isAccessTokenExpired(
    safetyBufferSeconds: Int = 30
) = getToken().mapSuccess { it.isExpired(safetyBufferSeconds = safetyBufferSeconds) }

/**
 * Maps the success value of a [Result] to a new value using the provided [transform].
 */
private inline fun <T> Result<AuthToken, AppError>.mapSuccess(
    transform: (AuthToken) -> T
) = if (isSuccess()) Result.Success(transform(data)) else this
