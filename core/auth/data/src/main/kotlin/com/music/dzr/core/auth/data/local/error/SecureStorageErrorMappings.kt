package com.music.dzr.core.auth.data.local.error

import androidx.datastore.core.CorruptionException
import com.music.dzr.core.auth.data.local.security.EncryptorException
import com.music.dzr.core.storage.error.StorageError
import java.io.IOException

/**
 * Maps an [Exception] to a [StorageError] during a write or update operation.
 */
internal fun Exception.toStorageErrorOnUpdate(): StorageError {
    return when (this) {
        is EncryptorException.Initialization -> SecureStorageError.InitializationFailed(this)
        is EncryptorException.Encryption -> SecureStorageError.EncryptionFailed(this)
        is IOException -> StorageError.WriteFailed(this)
        else -> StorageError.Unexpected(this)
    }
}

/**
 * Maps an [Exception] to a [StorageError] during a read operation.
 */
internal fun Exception.toStorageErrorOnRead(): StorageError {
    return when (this) {
        is EncryptorException.Initialization -> SecureStorageError.InitializationFailed(this)
        is EncryptorException.Decryption -> SecureStorageError.DecryptionFailed(this)
        is CorruptionException -> StorageError.DataCorrupted(this)
        is IOException -> StorageError.ReadFailed(this)
        else -> StorageError.Unexpected(this)
    }
}
