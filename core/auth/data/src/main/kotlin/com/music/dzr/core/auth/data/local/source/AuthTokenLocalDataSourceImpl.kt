package com.music.dzr.core.auth.data.local.source

import androidx.datastore.core.DataStore
import com.music.dzr.core.auth.data.local.error.SecureStorageError
import com.music.dzr.core.auth.data.local.error.toStorageErrorOnRead
import com.music.dzr.core.auth.data.local.error.toStorageErrorOnUpdate
import com.music.dzr.core.auth.data.local.model.AuthToken
import com.music.dzr.core.result.Result
import com.music.dzr.core.storage.error.StorageError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first

/**
 * DataStore-backed implementation of [AuthTokenLocalDataSource] with a suspend,
 * non-throwing Result-based API. Performs atomic writes via updateData,
 * maps read/write errors to [StorageError] (and [SecureStorageError]),
 * treats absent data as NotFound, and propagates cancellation via rethrow.
 */
internal class AuthTokenLocalDataSourceImpl(
    private val dataStore: DataStore<AuthToken>
) : AuthTokenLocalDataSource {

    override suspend fun get(): Result<AuthToken, StorageError> {
        return try {
            val data = dataStore.data.first()
            return if (data == EmptyToken) {
                Result.Failure(StorageError.NotFound)
            } else {
                Result.Success(data)
            }
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            Result.Failure(exception.toStorageErrorOnRead())
        }
    }

    override suspend fun save(token: AuthToken): Result<Unit, StorageError> {
        return try {
            dataStore.updateData { token }
            Result.Success(Unit)
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            Result.Failure(exception.toStorageErrorOnUpdate())
        }
    }

    override suspend fun clear(): Result<Unit, StorageError> {
        return save(EmptyToken)
    }

    companion object {
        private val EmptyToken get() = AuthToken.getDefaultInstance()
    }
}
