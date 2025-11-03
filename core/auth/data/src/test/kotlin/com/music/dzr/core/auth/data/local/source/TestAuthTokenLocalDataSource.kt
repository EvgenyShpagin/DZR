package com.music.dzr.core.auth.data.local.source

import com.music.dzr.core.auth.data.local.model.AuthToken
import com.music.dzr.core.data.test.HasForcedError
import com.music.dzr.core.data.test.runUnlessForcedError
import com.music.dzr.core.result.Result
import com.music.dzr.core.storage.error.StorageError
import java.util.concurrent.atomic.AtomicReference

/**
 * Configurable in-memory test implementation of [AuthTokenLocalDataSource].
 *
 * Set [forcedError] to return failures.
 *
 * Not thread-safe.
 */
class TestAuthTokenLocalDataSource : AuthTokenLocalDataSource, HasForcedError<StorageError> {

    private val tokenStore = AtomicReference<AuthToken>()
    override var forcedError: StorageError? = null

    override suspend fun get(): Result<AuthToken, StorageError> = runUnlessForcedError {
        return when (val token = tokenStore.get()) {
            null -> Result.Failure(StorageError.NotFound)
            else -> Result.Success(token)
        }
    }

    override suspend fun save(token: AuthToken): Result<Unit, StorageError> = runUnlessForcedError {
        tokenStore.set(token)
        Result.Success(Unit)
    }

    override suspend fun clear(): Result<Unit, StorageError> = runUnlessForcedError {
        tokenStore.set(null)
        Result.Success(Unit)
    }
}
