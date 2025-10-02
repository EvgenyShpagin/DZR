package com.music.dzr.core.auth.data.local.source

import com.music.dzr.core.auth.data.local.error.SecureStorageError
import com.music.dzr.core.auth.data.local.model.AuthToken
import com.music.dzr.core.data.error.StorageError
import com.music.dzr.core.result.Result

/**
 * Local persistence of OAuth token payload.
 *
 * Notes:
 * - Suspend, non-throwing API.
 *   Failures: [Result.Failure] with [StorageError] (incl. [SecureStorageError]).
 * - Writes should be atomic.
 */
internal interface AuthTokenLocalDataSource {
    /**
     * Read the stored token payload.
     *
     * @return [Result.Success] with token when present or [Result.Failure]
     * with an [SecureStorageError] or base [StorageError].
     */
    suspend fun get(): Result<AuthToken, StorageError>

    /**
     * Persist the provided token payload.
     *
     * @param token Network DTO with access token information to be stored.
     * @return [Result.Success] on success, or [Result.Failure]
     * with an [SecureStorageError] or base [StorageError].
     */
    suspend fun save(token: AuthToken): Result<Unit, StorageError>

    /**
     * Remove any stored token-related data from the local storage.
     *
     * @return [Result.Success] on success, or [Result.Failure]
     * with an [SecureStorageError] or base [StorageError].
     */
    suspend fun clear(): Result<Unit, StorageError>
}
