package com.music.dzr.core.auth.data.remote.oauth

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/**
 * A production-grade implementation of [PkceGenerator] that uses [SecureRandom] for
 * cryptographic randomness and [MessageDigest] for hashing.
 *
 * This implementation is thread-safe.
 */
internal class PkceGeneratorImpl : PkceGenerator {

    private val secureRandom = SecureRandom()

    override fun generateCodeVerifier(length: Int): String {
        require(length in CODE_VERIFIER_LENGTH_RANGE) {
            "Code verifier length must be in $CODE_VERIFIER_LENGTH_RANGE characters."
        }
        return buildString(length) {
            repeat(length) {
                // Use uniform selection to avoid modulo bias.
                val index = secureRandom.nextInt(CODE_VERIFIER_CHARSET.length)
                append(CODE_VERIFIER_CHARSET[index])
            }
        }
    }

    override fun generateCodeChallenge(verifier: String): String {
        val algorithm = MessageDigest.getInstance("SHA-256")
        val hash = algorithm.digest(verifier.toByteArray(Charsets.US_ASCII))
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
    }

    override fun generateState(bytes: Int): String {
        val buffer = ByteArray(bytes)
        secureRandom.nextBytes(buffer)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer)
    }

    private companion object {
        /**
         * The set of unreserved characters allowed in a PKCE code verifier, as per RFC 7636.
         */
        private const val CODE_VERIFIER_CHARSET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"

        /**
         * The valid length range for a code verifier, as per RFC 7636.
         */
        private val CODE_VERIFIER_LENGTH_RANGE = 43..128
    }
}
