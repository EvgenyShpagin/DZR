package com.music.dzr.core.auth.data.remote.oauth

import java.security.MessageDigest
import java.util.Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class OAuthSecurityProviderImplTest {

    private val provider: OAuthSecurityProvider = OAuthSecurityProviderImpl()

    @Test
    fun generateCodeVerifier_returnsRequestedLengthAndAllowedCharset() {
        // Arrange
        val length = 64
        val allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~".toSet()

        // Act
        val verifier = provider.generateCodeVerifier(length)

        // Assert
        assertEquals(length, verifier.length)
        assertTrue(verifier.all { it in allowed }, "Verifier contains only unreserved characters")
    }

    @Test
    fun generateCodeVerifier_enforcesRfcLengthBounds() {
        // Arrange
        val tooShort = 10
        val tooLong = 1024

        // Act + Assert
        assertFailsWith<IllegalArgumentException> { provider.generateCodeVerifier(tooShort) }
        assertFailsWith<IllegalArgumentException> { provider.generateCodeVerifier(tooLong) }
    }

    @Test
    fun generateCodeChallenge_matchesHashOfVerifier() {
        // Arrange
        val verifier = provider.generateCodeVerifier(64)
        val expected = run {
            val sha256 = MessageDigest.getInstance("SHA-256")
            val hash = sha256.digest(verifier.toByteArray(Charsets.US_ASCII))
            Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
        }

        // Act
        val challenge = provider.deriveCodeChallengeS256(verifier)

        // Assert
        assertEquals(expected, challenge)
    }

    @Test
    fun generateState_returnsUrlSafeBase64WithoutPadding() {
        // Arrange
        val bytes = 32

        // Act
        val state = provider.generateCsrfState(bytes)

        // Assert
        assertTrue(state.isNotBlank())
        assertTrue(
            !state.contains('+') && !state.contains('/') && !state.contains('='),
            "State must be URL-safe Base64 without padding"
        )
    }

    @Test
    fun generateCodeChallenge_matchesRfc7636S256Example() {
        // Arrange (RFC7636 S256 example)
        val verifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"
        val expectedChallenge = "E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM"

        // Act
        val actual = provider.deriveCodeChallengeS256(verifier)

        // Assert
        assertEquals(expectedChallenge, actual)
    }
}