package com.music.dzr.core.auth.data.local.error

import com.music.dzr.core.error.AppError

/**
 * A sealed hierarchy of errors produced by the local auth storage layer
 * (Preferences DataStore + Android Keystore–backed encryption).
 *
 * These errors are intentionally domain-oriented so that upper layers can
 * react appropriately (e.g., re-auth on key invalidation, retry on IO).
 */
internal sealed class AuthStorageError() : AppError {
    /**
     * An optional cause of the error to provide more context in upper layers.
     */
    open val cause: Throwable? = null

    /**
     * No token persisted in storage. A normal, expected state.
     */
    data object NotFound : AuthStorageError()

    /**
     * Integrity/authenticity check failed during decrypt (e.g., AES-GCM tag mismatch).
     * Typical causes: tampered/corrupted ciphertext, wrong key material.
     */
    data class IntegrityCheckFailed(override val cause: Throwable?) : AuthStorageError()

    /**
     * Stored data is malformed or inconsistent (e.g., invalid Base64, missing required fields).
     */
    data class DataCorrupted(override val cause: Throwable?) : AuthStorageError()

    /**
     * Keystore key became permanently invalid (e.g., device lock changed, biometric reset).
     * Requires re-authentication and key regeneration.
     */
    data object KeyInvalidated : AuthStorageError()

    /**
     * Generic cryptographic failure not covered above (Cipher/Keystore errors).
     */
    data class CryptoFailure(override val cause: Throwable?) : AuthStorageError()

    /**
     * Reading from DataStore failed (IO/state issues).
     */
    data class ReadFailed(override val cause: Throwable?) : AuthStorageError()

    /**
     * Writing to DataStore failed (IO/state issues).
     */
    data class WriteFailed(override val cause: Throwable?) : AuthStorageError()

    /**
     * Fallback for unexpected cases. Inspect [cause] for details.
     */
    data class Unknown(override val cause: Throwable?) : AuthStorageError()
}