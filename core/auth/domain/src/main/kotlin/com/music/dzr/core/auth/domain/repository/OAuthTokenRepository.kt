package com.music.dzr.core.auth.domain.repository

import com.music.dzr.core.auth.domain.model.OAuthToken

/**
 * An interface that defines the contract for storing, retrieving, and managing OAuth tokens.
 *
 * This repository is responsible for the persistence of authentication state.
 * Implementations of this interface will handle the actual storage mechanism.
 */
interface OAuthTokenRepository {

    /**
     * Saves the complete token grant.
     *
     * The implementation should persist the necessary parts of the token. If the refreshToken
     * in the provided [token] is null, the existing refresh token should be preserved.
     *
     * @param token The [OAuthToken] domain model to save.
     */
    suspend fun saveToken(token: OAuthToken)

    /**
     * Retrieves the current access token.
     *
     * @return The access token as a [String], or `null` if no token is available.
     */
    suspend fun getAccessToken(): String?

    /**
     * Retrieves the current token type (e.g., "Bearer").
     *
     * @return The token type as a [String], or `null` if no token is available.
     */
    suspend fun getTokenType(): String?

    /**
     * Retrieves the current refresh token.
     *
     * @return The refresh token as a [String], or `null` if no token is available.
     */
    suspend fun getRefreshToken(): String?

    /**
     * Clears all stored tokens, effectively logging the user out.
     */
    suspend fun clearTokens()
} 