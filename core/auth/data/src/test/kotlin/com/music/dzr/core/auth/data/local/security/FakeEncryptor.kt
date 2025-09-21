package com.music.dzr.core.auth.data.local.security

/**
 * Simple fake Encryptor for unit tests (AAA-friendly, no framework needed)
 */
internal class FakeEncryptor : Encryptor {
    var failOnEncrypt: Boolean = false
    var failOnDecrypt: Boolean = false

    override fun encrypt(plainText: String): String {
        if (failOnEncrypt) throw RuntimeException("encrypt failed")
        return "enc:$plainText"
    }

    override fun decrypt(cipherText: String): String {
        if (failOnDecrypt) throw RuntimeException("decrypt failed")
        return cipherText.removePrefix("enc:")
    }
}