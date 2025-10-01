package com.music.dzr.core.auth.data.local.error

import com.music.dzr.core.data.error.StorageError

/**
 * An abstraction for failures related to secure, encrypted storage.
 *
 * @see StorageError
 */
internal sealed class SecureStorageError : StorageError() {
    /** Failed to initialize cryptographic primitives or create/obtain keys. */
    data class InitializationFailed(override val cause: Throwable) : SecureStorageError()

    /** Failed to encrypt plaintext prior to persistence. */
    data class EncryptionFailed(override val cause: Throwable) : SecureStorageError()

    /** Failed to decrypt previously persisted ciphertext. */
    data class DecryptionFailed(override val cause: Throwable) : SecureStorageError()
}
