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

    override fun encrypt(plainBytes: ByteArray): ByteArray {
        if (failOnEncrypt) throw RuntimeException("encrypt failed")
        val prefix = "enc:".toByteArray(Charsets.UTF_8)
        return prefix + plainBytes
    }

    override fun decrypt(cipherText: String): String {
        if (failOnDecrypt) throw RuntimeException("decrypt failed")
        return cipherText.removePrefix("enc:")
    }

    override fun decrypt(cipherBytes: ByteArray): ByteArray {
        if (failOnDecrypt) throw RuntimeException("decrypt failed")
        val prefix = "enc:".toByteArray(Charsets.UTF_8)
        return if (cipherBytes.startsWith(prefix))
            cipherBytes.drop(prefix.size).toByteArray()
        else
            cipherBytes
    }

    private fun ByteArray.startsWith(prefix: ByteArray): Boolean =
        size >= prefix.size && copyOfRange(0, prefix.size).contentEquals(prefix)
}