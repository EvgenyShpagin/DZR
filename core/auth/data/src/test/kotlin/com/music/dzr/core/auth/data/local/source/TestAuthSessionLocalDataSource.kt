package com.music.dzr.core.auth.data.local.source

import com.music.dzr.core.auth.data.local.model.AuthSession
import com.music.dzr.core.data.test.HasForcedError
import com.music.dzr.core.data.test.runUnlessForcedError
import com.music.dzr.core.result.Result
import com.music.dzr.core.storage.error.StorageError
import java.util.concurrent.atomic.AtomicReference

/**
 * Configurable in-memory test implementation of [AuthSessionLocalDataSource].
 *
 * Set [forcedError] to return failures.
 *
 * Not thread-safe.
 */
class TestAuthSessionLocalDataSource : AuthSessionLocalDataSource, HasForcedError<StorageError> {

    private val sessionStore = AtomicReference<AuthSession>()
    override var forcedError: StorageError? = null

    override suspend fun save(
        authSession: AuthSession
    ): Result<Unit, StorageError> = runUnlessForcedError {
        sessionStore.set(authSession)
        Result.Success(Unit)
    }

    override suspend fun get(): Result<AuthSession, StorageError> = runUnlessForcedError {
        return when (val session = sessionStore.get()) {
            null -> Result.Failure(StorageError.NotFound)
            else -> Result.Success(session)
        }
    }

    override suspend fun clear(): Result<Unit, StorageError> = runUnlessForcedError {
        sessionStore.set(null)
        Result.Success(Unit)
    }
}
