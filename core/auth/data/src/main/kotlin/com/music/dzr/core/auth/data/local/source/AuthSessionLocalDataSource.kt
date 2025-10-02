package com.music.dzr.core.auth.data.local.source

import com.music.dzr.core.auth.data.local.error.SecureStorageError
import com.music.dzr.core.auth.data.local.model.AuthSession
import com.music.dzr.core.result.Result
import com.music.dzr.core.storage.error.StorageError

/**
 * A data source for managing a short-lived authorization session, which includes
 * parameters like the PKCE verifier and OAuth state.
 *
 * Persisting these values is crucial for the authorization flow to withstand
 * process death, ensuring a seamless user experience.
 *
 * Implementations of this interface must prioritize security by storing data
 * securely and preventing any leakage of sensitive information into logs.
 */
internal interface AuthSessionLocalDataSource {

    /**
     * Saves the provided authorization session.
     *
     * @param authSession The [AuthSession] containing the authorization data to be stored.
     * @return [Result.Success] on successful save, or [Result.Failure]
     *  with a [SecureStorageError] or a base [StorageError] if an error occurs.
     */
    suspend fun save(authSession: AuthSession): Result<Unit, StorageError>

    /**
     * Retrieves the current authorization session.
     *
     * @return [Result.Success] with the [AuthSession] if found, or [Result.Failure]
     *  with an [StorageError.NotFound] if no session is stored, or another
     *  [SecureStorageError]/[StorageError] if a different error occurs.
     */
    suspend fun get(): Result<AuthSession, StorageError>

    /**
     * Clears any stored authorization session data.
     *
     * @return [Result.Success] on successful deletion, or [Result.Failure]
     *  with a [SecureStorageError] or a base [StorageError] if an error occurs.
     */
    suspend fun clear(): Result<Unit, StorageError>
}
