package com.music.dzr.core.auth.data.remote.http

import com.music.dzr.core.auth.data.repository.TestTokenRepository
import com.music.dzr.core.auth.domain.error.AuthError
import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.model.AuthToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var client: OkHttpClient
    private lateinit var tokenRepository: TestTokenRepository

    @BeforeTest
    fun setUp() {
        server = MockWebServer()
        server.start()
        tokenRepository = TestTokenRepository()
        client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenRepository))
            .build()
    }

    @AfterTest
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun addsAuthorizationHeader_whenTokenIsAvailable() {
        // Arrange
        val accessToken = "test_access_token"
        tokenRepository.setTokens(accessToken, "any_refresh_token")
        server.enqueue(MockResponse())
        val request = Request.Builder().url(server.url("/")).build()

        // Act
        client.newCall(request).execute()

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("Bearer $accessToken", recordedRequest.getHeader("Authorization"))
    }

    @Test
    fun doesNotAddAuthorizationHeader_whenTokenIsNull() {
        // Arrange
        tokenRepository.resetTokens()
        server.enqueue(MockResponse())
        val request = Request.Builder().url(server.url("/")).build()

        // Act
        client.newCall(request).execute()

        // Assert
        val recordedRequest = server.takeRequest()
        assertNull(recordedRequest.getHeader("Authorization"))
    }

    @Test
    fun doesNotAddAuthorizationHeader_whenAlreadyAdded() {
        // Arrange
        val accessToken = "test_access_token"
        tokenRepository.setTokens(accessToken, "any_refresh_token")
        server.enqueue(MockResponse())
        val request = Request.Builder().url(server.url("/"))
            .header("Authorization", "Bearer test_access_token")
            .build()

        // Act
        client.newCall(request).execute()

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("Bearer $accessToken", recordedRequest.getHeader("Authorization"))
    }

    @Test
    fun addsAuthorizationHeader_afterRefreshingExpiredToken() {
        // Arrange: repository with expired token and a refresh token available
        val expiredToken = AuthToken(
            accessToken = "expired",
            tokenType = "Bearer",
            expiresInSeconds = 3600,
            refreshToken = "REFRESH",
            scopes = listOf(AuthScope("scope")),
            expiresAtMillis = System.currentTimeMillis() - 1 // already expired
        )

        // We need a fresh client wired to this repository
        tokenRepository = TestTokenRepository(defaultToken = expiredToken).apply {
            tokenAfterRefresh = AuthToken(
                accessToken = "new_access",
                tokenType = "Bearer",
                expiresInSeconds = 3600,
                refreshToken = "NEW_REFRESH",
                scopes = emptyList()
            )
        }
        client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenRepository))
            .build()

        server.enqueue(MockResponse())
        val request = Request.Builder().url(server.url("/"))
            .build()

        // Act
        client.newCall(request).execute()

        // Assert: header uses refreshed token
        val recordedRequest = server.takeRequest()
        assertEquals("Bearer new_access", recordedRequest.getHeader("Authorization"))
    }

    @Test
    fun doesNotAddAuthorizationHeader_whenExpiredAndRefreshFails() {
        // Arrange: expired token but refresh fails
        val expiredToken = AuthToken(
            accessToken = "expired",
            tokenType = "bearer",
            expiresInSeconds = 3600,
            refreshToken = "REFRESH",
            scopes = emptyList(),
            expiresAtMillis = System.currentTimeMillis() - 1
        )
        tokenRepository = TestTokenRepository(defaultToken = expiredToken).apply {
            forcedError = AuthError.NotAuthenticated
        }
        client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenRepository))
            .build()

        server.enqueue(MockResponse())
        val request = Request.Builder().url(server.url("/"))
            .build()

        // Act
        client.newCall(request).execute()

        // Assert: no Authorization header added when refresh fails
        val recorded = server.takeRequest()
        assertNull(recorded.getHeader("Authorization"))
    }
}