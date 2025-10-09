package com.music.dzr.core.auth.domain.repository

import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.model.AuthToken
import com.music.dzr.core.error.AppError
import com.music.dzr.core.error.PersistenceError
import com.music.dzr.core.result.Result

/**
 * An interface that defines the contract for storing, retrieving, and managing OAuth tokens.
 *
 * This repository is responsible for the persistence of authentication state.
 * Implementations of this interface will handle the actual storage mechanism.
 */
interface AuthTokenRepository {

    /**
     * Retrieves authentication token.
     *
     * @return The current [AuthToken] wrapped in a [Result], or one of these errors on failure:
     * - [com.music.dzr.core.auth.domain.error.AuthError],
     * - [com.music.dzr.core.error.PersistenceError].
     */
    suspend fun getToken(): Result<AuthToken, AppError>

    /**
     * Saves the complete token grant.
     *
     * The implementation should persist the necessary parts of the token. If the refreshToken
     * in the provided [token] is null, the existing refresh token should be preserved.
     *
     * @param token The [AuthToken] domain model to save.
     * @return A [Result] indicating success, or one of these errors on failure:
     * - [com.music.dzr.core.auth.domain.error.AuthError],
     * - [com.music.dzr.core.error.PersistenceError].
     */
    suspend fun saveToken(token: AuthToken): Result<Unit, AppError>

    /**
     * Attempts to refresh the access token using the current refresh token.
     * Saves the new token on success or clears tokens on unrecoverable failure.
     *
     * @return A [Result] indicating success, or one of these errors on failure:
     * - [com.music.dzr.core.auth.domain.error.AuthError],
     * - [com.music.dzr.core.error.ConnectivityError],
     * - [com.music.dzr.core.error.NetworkError]
     */
    suspend fun refreshToken(): Result<Unit, AppError>

    /**
     * Clears all stored tokens, effectively logging the user out.
     *
     * @return A [Result] indicating success, or one of these errors on failure:
     * - [com.music.dzr.core.auth.domain.error.AuthError],
     * - [com.music.dzr.core.error.ConnectivityError],
     * - [com.music.dzr.core.error.NetworkError]
     */
    suspend fun clearTokens(): Result<Unit, AppError>

}
