package com.music.dzr.core.auth.data.local.security

/**
 * Abstraction for encrypting/decrypting short text values to be stored in local storage.
 */
internal interface Encryptor {
    fun encrypt(plainText: String): String
    fun decrypt(cipherText: String): String
}
