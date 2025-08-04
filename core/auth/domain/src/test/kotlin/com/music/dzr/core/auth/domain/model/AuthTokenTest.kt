package com.music.dzr.core.auth.domain.model

import java.util.concurrent.TimeUnit
import kotlin.test.DefaultAsserter.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthTokenTest {

    @Test
    fun initializesSuccessfully_whenFieldsAreValid() {
        // Arrange
        val accessToken = "validAccessToken"
        val tokenType = "Bearer"
        val expiresInSeconds = 3600
        val refreshToken = "validRefreshToken"
        val scopes = listOf(AuthScope("read"), AuthScope("write"))

        // Act
        val token = AuthToken(
            accessToken = accessToken,
            tokenType = tokenType,
            expiresInSeconds = expiresInSeconds,
            refreshToken = refreshToken,
            scopes = scopes
        )

        // Assert
        assertEquals(accessToken, token.accessToken)
        assertEquals(tokenType, token.tokenType)
        assertEquals(expiresInSeconds, token.expiresInSeconds)
        assertEquals(refreshToken, token.refreshToken)
        assertEquals(scopes, token.scopes)
    }

    @Test
    fun throwsException_whenAccessTokenIsBlank() {
        // Arrange
        val accessToken = "  "
        val tokenType = "Bearer"
        val expiresInSeconds = 3600

        // Act & Assert
        val exception = assertFailsWith<IllegalArgumentException> {
            AuthToken(
                accessToken = accessToken,
                tokenType = tokenType,
                expiresInSeconds = expiresInSeconds,
                refreshToken = null,
                scopes = null
            )
        }
        assertEquals("Access token cannot be blank", exception.message)
    }

    @Test
    fun throwsException_whenTokenTypeIsBlank() {
        // Arrange
        val accessToken = "validAccessToken"
        val tokenType = " "
        val expiresInSeconds = 3600

        // Act & Assert
        val exception = assertFailsWith<IllegalArgumentException> {
            AuthToken(
                accessToken = accessToken,
                tokenType = tokenType,
                expiresInSeconds = expiresInSeconds,
                refreshToken = null,
                scopes = null
            )
        }
        assertEquals("Token type cannot be blank", exception.message)
    }

    @Test
    fun throwsException_whenExpiresInSecondsIsNonPositive() {
        // Arrange
        val accessToken = "validAccessToken"
        val tokenType = "Bearer"
        val expiresInSeconds = 0

        // Act & Assert
        val exception = assertFailsWith<IllegalArgumentException> {
            AuthToken(
                accessToken = accessToken,
                tokenType = tokenType,
                expiresInSeconds = expiresInSeconds,
                refreshToken = null,
                scopes = null
            )
        }
        assertEquals("Expires in seconds must be positive", exception.message)
    }

    @Test
    fun calculatesExpiryTimeCorrectly_whenGivenValidExpiresInSeconds() {
        // Arrange
        val accessToken = "validAccessToken"
        val tokenType = "Bearer"
        val expiresInSeconds = 300

        // Act
        val token = AuthToken(
            accessToken = accessToken,
            tokenType = tokenType,
            expiresInSeconds = expiresInSeconds,
            refreshToken = null,
            scopes = null
        )
        val currentTimeMillis = System.currentTimeMillis()
        val expectedExpiryTime =
            currentTimeMillis + TimeUnit.SECONDS.toMillis(expiresInSeconds.toLong())

        // Assert
        assertTrue(
            "Expiry time is not calculated correctly",
            token.expiresAtMillis in expectedExpiryTime - 100..expectedExpiryTime + 100,
        )
    }
}