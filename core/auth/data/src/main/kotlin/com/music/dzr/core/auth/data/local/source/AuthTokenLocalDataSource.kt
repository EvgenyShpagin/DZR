package com.music.dzr.core.auth.data.local.source

import com.music.dzr.core.auth.data.remote.dto.AuthToken
import com.music.dzr.core.auth.data.local.error.AuthStorageError
import com.music.dzr.core.result.Result

/**
 * Abstraction for persisting OAuth token payload locally.
 */
internal interface AuthTokenLocalDataSource {
    /**
     * Persist the provided token payload.
     *
     * @param token Network DTO with access token information to be stored.
     * @return [Result.Success] on success, or [Result.Failure] with a [AuthStorageError] on failure.
     */
    suspend fun saveToken(token: AuthToken): Result<Unit, AuthStorageError>

    /**
     * Remove any stored token-related data from the local storage.
     *
     * @return [Result.Success] on success, or [Result.Failure] with a [AuthStorageError] on failure.
     */
    suspend fun clearTokens(): Result<Unit, AuthStorageError>
}
