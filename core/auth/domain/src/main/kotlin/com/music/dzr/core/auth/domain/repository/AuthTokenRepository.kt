package com.music.dzr.core.auth.domain.repository

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
