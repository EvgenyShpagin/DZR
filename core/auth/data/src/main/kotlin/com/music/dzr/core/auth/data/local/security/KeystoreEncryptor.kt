package com.music.dzr.core.auth.data.local.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.music.dzr.core.auth.data.local.security.EncryptorException.Decryption
import com.music.dzr.core.auth.data.local.security.EncryptorException.Encryption
import com.music.dzr.core.auth.data.local.security.EncryptorException.Initialization
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlin.coroutines.cancellation.CancellationException

/**
 * AES/GCM encryptor backed by Android Keystore.
 * Stores payload as iv || ciphertext, with a random IV per encryption.
 */
internal class KeystoreEncryptor(private val keyAlias: String) : Encryptor {

    override fun encrypt(plainBytes: ByteArray): ByteArray = wrapExceptionsIn(::Encryption) {
        val key = getOrCreateKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val cipherBytes = cipher.doFinal(plainBytes)
        return iv + cipherBytes
    }

    override fun decrypt(cipherBytes: ByteArray): ByteArray = wrapExceptionsIn(::Decryption) {
        require(cipherBytes.size >= IV_SIZE + GCM_TAG_LENGTH_BYTES) { "Corrupted encrypted payload" }
        val iv = cipherBytes.copyOfRange(0, IV_SIZE)
        val cipherData = cipherBytes.copyOfRange(IV_SIZE, cipherBytes.size)
        val key = getOrCreateKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        return cipher.doFinal(cipherData)
    }

    private fun getOrCreateKey(): SecretKey = wrapExceptionsIn(::Initialization) {
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

    /**
     * A wrapper to catch all exceptions (except CancellationException) from a block
     * and rethrow them as a specific [EncryptorException].
     */
    private inline fun <T> wrapExceptionsIn(
        wrapException: (cause: Throwable) -> EncryptorException,
        block: () -> T
    ): T {
        try {
            return block()
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            throw wrapException(exception)
        }
    }

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val IV_SIZE = 12 // GCM standard IV length
        private const val GCM_TAG_LENGTH_BITS = 128
        private const val GCM_TAG_LENGTH_BYTES = GCM_TAG_LENGTH_BITS / 8
    }
}
