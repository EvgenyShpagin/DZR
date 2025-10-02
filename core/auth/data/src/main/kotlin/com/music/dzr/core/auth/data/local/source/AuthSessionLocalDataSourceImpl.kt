package com.music.dzr.core.auth.data.local.source

import androidx.datastore.core.DataStore
import com.music.dzr.core.auth.data.local.error.SecureStorageError
import com.music.dzr.core.auth.data.local.error.toStorageErrorOnRead
import com.music.dzr.core.auth.data.local.error.toStorageErrorOnUpdate
import com.music.dzr.core.auth.data.local.model.AuthSession
import com.music.dzr.core.result.Result
import com.music.dzr.core.storage.error.StorageError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first

/**
 * DataStore-backed implementation of [AuthSessionLocalDataSource] with a suspend,
 * non-throwing Result-based API. Performs atomic writes via updateData,
 * maps read/write errors to [StorageError] (and [SecureStorageError]),
 * treats absent data as NotFound, and propagates cancellation via rethrow.
 */
internal class AuthSessionLocalDataSourceImpl(
    private val dataStore: DataStore<AuthSession>,
) : AuthSessionLocalDataSource {

    override suspend fun save(authSession: AuthSession): Result<Unit, StorageError> {
        return try {
            dataStore.updateData { authSession }
            Result.Success(Unit)
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            Result.Failure(exception.toStorageErrorOnUpdate())
        }
    }

    override suspend fun get(): Result<AuthSession, StorageError> {
        return try {
            val data = dataStore.data.first()
            if (data == EmptySession) {
                Result.Failure(StorageError.NotFound)
            } else {
                Result.Success(data)
            }
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            return Result.Failure(exception.toStorageErrorOnRead())
        }
    }

    override suspend fun clear(): Result<Unit, StorageError> {
        return save(EmptySession)
    }

    companion object {
        private val EmptySession get() = AuthSession.getDefaultInstance()
    }
}
