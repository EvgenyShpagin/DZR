package com.music.dzr.core.auth.data.local.security

/**
 * A specific exception wrapper for errors originating from the Encryptor component.
 * This allows consumers to distinguish encryption-related failures from other exceptions
 * like I/O errors from a DataStore.
 *
 * @param cause The original low-level exception (e.g., KeyStoreException, AEADBadTagException).
 */
sealed class EncryptorException(cause: Throwable) : Exception(cause) {
    /** Thrown when the encryption/decryption key cannot be initialized or retrieved. */
    class Initialization(cause: Throwable) : EncryptorException(cause)

    /** Thrown when data encryption fails. */
    class Encryption(cause: Throwable) : EncryptorException(cause)

    /** Thrown when data decryption fails. */
    class Decryption(cause: Throwable) : EncryptorException(cause)
}
