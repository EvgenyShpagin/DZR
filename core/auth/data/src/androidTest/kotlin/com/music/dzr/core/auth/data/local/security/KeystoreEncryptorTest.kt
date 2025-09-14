package com.music.dzr.core.auth.data.local.security

import android.util.Base64
import com.music.dzr.core.auth.data.local.security.KeystoreEncryptor
import kotlin.experimental.xor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertFailsWith

class KeystoreEncryptorTest {

    private fun newEncryptor(
        aliasSuffix: String = System.nanoTime().toString()
    ): KeystoreEncryptor {
        // Use a unique alias per test to avoid cross-test interference
        return KeystoreEncryptor("test_keystore_encryptor_$aliasSuffix")
    }

    @Test
    fun encryptDecrypt_roundTrip_returnsOriginal() {
        // Arrange
        val encryptor = newEncryptor()
        val original = "hello-Ωauth-🚀-${System.currentTimeMillis()}"
        // Act
        val encoded = encryptor.encrypt(original)
        val decoded = encryptor.decrypt(encoded)
        // Assert
        assertEquals(original, decoded)
    }

    @Test
    fun encrypt_usesRandomIv_producesDifferentCiphertexts() {
        // Arrange
        val encryptor = newEncryptor()
        val original = "same-input"
        // Act
        val c1 = encryptor.encrypt(original)
        val c2 = encryptor.encrypt(original)
        // Assert
        assertNotEquals(c1, c2, "Ciphertexts should differ due to random IV")
        assertEquals(original, encryptor.decrypt(c1))
        assertEquals(original, encryptor.decrypt(c2))
    }

    @Test
    fun decrypt_withCorruptedPayload_throws() {
        // Arrange
        val encryptor = newEncryptor()
        val original = "payload-to-corrupt"
        val encoded = encryptor.encrypt(original)
        // Act: make it invalid Base64 by trimming
        val corrupted = encoded.dropLast(4)
        // Assert
        assertFailsWith<Exception> { encryptor.decrypt(corrupted) }
    }

    @Test
    fun decrypt_withDifferentAlias_throws() {
        // Arrange
        val e1 = newEncryptor("a")
        val e2 = newEncryptor("b")
        val original = "cross-alias"
        val c = e1.encrypt(original)
        // Act + Assert
        assertFailsWith<Exception> { e2.decrypt(c) }
    }

    @Test
    fun encryptDecrypt_emptyString_supported() {
        // Arrange
        val encryptor = newEncryptor()
        val original = ""
        // Act
        val enc = encryptor.encrypt(original)
        val dec = encryptor.decrypt(enc)
        // Assert
        assertEquals(original, dec)
    }

    @Test
    fun encryptDecrypt_longUnicode_supported() {
        // Arrange
        val encryptor = newEncryptor()
        val original = "😀🚀✨ — длинная строка — ".repeat(100)
        // Act
        val enc = encryptor.encrypt(original)
        val dec = encryptor.decrypt(enc)
        // Assert
        assertEquals(original, dec)
    }

    @Test
    fun decrypt_invalidBase64_throws() {
        // Arrange
        val encryptor = newEncryptor()
        val invalid = "not_base64!!"
        // Act + Assert
        assertFailsWith<Exception> { encryptor.decrypt(invalid) }
    }

    @Test
    fun decrypt_shortPayload_throws() {
        // Arrange
        val encryptor = newEncryptor()
        // Payload shorter than IV size will fail the size check
        val short = Base64.encodeToString(ByteArray(8) { 0 }, Base64.NO_WRAP)
        // Act + Assert
        assertFailsWith<IllegalArgumentException> { encryptor.decrypt(short) }
    }

    @Test
    fun decrypt_tamperedCiphertext_throws() {
        // Arrange
        val encryptor = newEncryptor()
        val original = "tamper-me"
        val enc = encryptor.encrypt(original)
        val bytes = Base64.decode(enc, Base64.NO_WRAP)
        // Flip a bit in ciphertext portion (after IV)
        if (bytes.size > 16) {
            bytes[bytes.lastIndex] = (bytes.last() xor 0x01.toByte())
        }
        val tampered = Base64.encodeToString(bytes, Base64.NO_WRAP)
        // Act + Assert — AES-GCM should fail authentication
        assertFailsWith<Exception> { encryptor.decrypt(tampered) }
    }
}
