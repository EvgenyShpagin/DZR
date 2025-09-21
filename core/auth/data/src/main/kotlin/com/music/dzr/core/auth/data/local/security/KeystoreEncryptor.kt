package com.music.dzr.core.auth.data.local.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * AES/GCM encryptor backed by Android Keystore.
 * Stores payload as Base64(iv || ciphertext), with a random IV per encryption.
 */
internal class KeystoreEncryptor(private val keyAlias: String) : Encryptor {

    override fun encrypt(plainText: String): String {
        val plainBytes = plainText.toByteArray(Charsets.UTF_8)
        val cipherBytes = encrypt(plainBytes)
        return Base64.encodeToString(cipherBytes, Base64.NO_WRAP)
    }

    override fun encrypt(plainBytes: ByteArray): ByteArray {
        val key = getOrCreateKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val cipherBytes = cipher.doFinal(plainBytes)
        return iv + cipherBytes
    }

    override fun decrypt(cipherText: String): String {
        val cipherBytes = Base64.decode(cipherText, Base64.NO_WRAP)
        val plainBytes = decrypt(cipherBytes)
        return plainBytes.toString(Charsets.UTF_8)
    }

    override fun decrypt(cipherBytes: ByteArray): ByteArray {
        require(cipherBytes.size >= IV_SIZE + GCM_TAG_LENGTH_BYTES) { "Corrupted encrypted payload" }
        val iv = cipherBytes.copyOfRange(0, IV_SIZE)
        val cipherBytes = cipherBytes.copyOfRange(IV_SIZE, cipherBytes.size)
        val key = getOrCreateKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        return cipher.doFinal(cipherBytes)
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val existing = keyStore.getKey(keyAlias, null) as? SecretKey
        if (existing != null) return existing

        val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val spec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true)
            .build()
        keyGen.init(spec)
        return keyGen.generateKey()
    }

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val IV_SIZE = 12 // GCM standard IV length
        private const val GCM_TAG_LENGTH_BITS = 128
        private const val GCM_TAG_LENGTH_BYTES = GCM_TAG_LENGTH_BITS / 8
    }
}
