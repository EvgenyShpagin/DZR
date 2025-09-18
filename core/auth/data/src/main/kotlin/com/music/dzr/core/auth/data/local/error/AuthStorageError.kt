package com.music.dzr.core.auth.data.local.error

import com.music.dzr.core.data.error.StorageError

/**
 * A sealed hierarchy of errors produced by the local auth storage layer
 * (Preferences DataStore + Android Keystore–backed encryption).
 *
 * These errors are intentionally domain-oriented so that upper layers can
 * react appropriately (e.g., re-auth on key invalidation, retry on IO).
 */
internal sealed class AuthStorageError() : StorageError() {
    /**
     * Integrity/authenticity check failed during decrypt (e.g., AES-GCM tag mismatch).
     * Typical causes: tampered/corrupted ciphertext, wrong key material.
     */
    data class IntegrityCheckFailed(override val cause: Throwable?) : AuthStorageError()

    /**
     * Keystore key became permanently invalid (e.g., device lock changed, biometric reset).
     * Requires re-authentication and key regeneration.
     */
    data object KeyInvalidated : AuthStorageError()

    /**
     * Generic cryptographic failure not covered above (Cipher/Keystore errors).
     */
    data class CryptoFailure(override val cause: Throwable?) : AuthStorageError()
}