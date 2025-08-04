package com.music.dzr.core.auth.domain.repository

import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.model.AuthToken

/**
 * An interface that defines the contract for storing, retrieving, and managing OAuth tokens.
 *
 * This repository is responsible for the persistence of authentication state.
 * Implementations of this interface will handle the actual storage mechanism.
 */
interface AuthTokenRepository {

    /**
     * Retrieves the current authentication token.
     *
     * @return The current [AuthToken], or `null` if no token is available.
     */
    suspend fun getToken(): AuthToken?

    /**
     * Saves the complete token grant.
     *
     * The implementation should persist the necessary parts of the token. If the refreshToken
     * in the provided [token] is null, the existing refresh token should be preserved.
     *
     * @param token The [AuthToken] domain model to save.
     */
    suspend fun saveToken(token: AuthToken)

    /**
     * Attempts to refresh the access token using the current refresh token.
     * Saves the new token on success or clears tokens on unrecoverable failure.
     *
     * @return `true` if the token was refreshed successfully, `false` otherwise.
     */
    suspend fun refreshToken(): Boolean

    /**
     * Clears all stored tokens, effectively logging the user out.
     */
    suspend fun clearTokens()
}

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
 *
 * @return `true` if the access token has expired or no token is available, `false` otherwise.
 */
suspend fun AuthTokenRepository.isAccessTokenExpired() = getToken()?.expiresAtMillis