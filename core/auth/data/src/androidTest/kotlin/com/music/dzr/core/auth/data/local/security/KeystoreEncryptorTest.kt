package com.music.dzr.core.auth.data.local.security

import kotlin.experimental.xor
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class KeystoreEncryptorTest {

    private fun createEncryptor(
        aliasSuffix: String = System.nanoTime().toString()
    ): KeystoreEncryptor {
        // Use a unique alias per test to avoid cross-test interference
        return KeystoreEncryptor("test_keystore_encryptor_$aliasSuffix")
    }

    @Test
    fun encryptDecrypt_roundTrip_returnsOriginal() {
        // Arrange
        val encryptor = createEncryptor()
        val plain = "hello-Ωauth-🚀1.5".toByteArray()
        // Act
        val cipher = encryptor.encrypt(plain)
        val decrypted = encryptor.decrypt(cipher)
        // Assert
        assertContentEquals(plain, decrypted)
    }

    @Test
    fun encrypt_usesRandomIv_producesDifferentCiphertexts() {
        // Arrange
        val encryptor = createEncryptor()
        val plain = "same-input".toByteArray()
        // Act
        val cipher1 = encryptor.encrypt(plain)
        val cipher2 = encryptor.encrypt(plain)
        // Assert
        assertNotEquals(
            cipher1.toList(),
            cipher2.toList(),
            "Ciphertexts should differ due to random IV"
        )
    }

    @Test
    fun decrypt_withRandomBytes_throws() {
        // Arrange
        val encryptor = createEncryptor()
        val plain = Random.nextBytes(32) // With correct length
        // Assert
        assertFailsWith<EncryptorException.Decryption> { encryptor.decrypt(plain) }
    }

    @Test
    fun decrypt_withDifferentAlias_throws() {
        // Arrange
        val encryptor1 = createEncryptor("a")
        val encryptor2 = createEncryptor("b")
        val plain = "cross-alias".toByteArray()
        val cipher = encryptor1.encrypt(plain)
        // Act + Assert
        assertFailsWith<EncryptorException.Decryption> { encryptor2.decrypt(cipher) }
    }

    @Test
    fun encryptDecrypt_emptyString_supported() {
        // Arrange
        val encryptor = createEncryptor()
        val plain = "".toByteArray()
        // Act
        val cipher = encryptor.encrypt(plain)
        val decrypted = encryptor.decrypt(cipher)
        // Assert
        assertContentEquals(plain, decrypted)
    }

    @Test
    fun encryptDecrypt_longUnicode_supported() {
        // Arrange
        val encryptor = createEncryptor()
        val plain = "😀🚀✨ — long string — ".repeat(100).toByteArray()
        // Act
        val cipher = encryptor.encrypt(plain)
        val decrypted = encryptor.decrypt(cipher)
        // Assert
        assertContentEquals(plain, decrypted)
    }

    @Test
    fun decrypt_shortPayload_throws() {
        // Arrange
        val encryptor = createEncryptor()
        val short = ByteArray(8) // Payload shorter than IV + tag size
        // Act + Assert
        assertFailsWith<EncryptorException.Decryption> { encryptor.decrypt(short) }
    }

    @Test
    fun decrypt_tamperedCiphertext_throws() {
        // Arrange
        val encryptor = createEncryptor()
        val plainBytes = "tamper-me".toByteArray()
        val enc = encryptor.encrypt(plainBytes)
        val modifiedBytes = enc.copyOf()
        // Flip a bit in ciphertext portion (after IV)
        if (modifiedBytes.size > 16) {
            modifiedBytes[modifiedBytes.lastIndex] = (modifiedBytes.last() xor 0x01.toByte())
        }
        // Act + Assert — AES-GCM should fail authentication
        assertFailsWith<EncryptorException.Decryption> { encryptor.decrypt(modifiedBytes) }
    }
}
