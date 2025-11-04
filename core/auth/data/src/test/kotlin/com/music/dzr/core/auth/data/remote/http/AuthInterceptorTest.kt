package com.music.dzr.core.auth.data.remote.http

import com.music.dzr.core.auth.data.repository.TestTokenRepository
import com.music.dzr.core.auth.domain.error.AuthError
import com.music.dzr.core.auth.domain.model.AuthToken
import kotlinx.coroutines.test.runTest
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
    fun addsAuthorizationHeader_whenTokenIsAvailable() = runTest {
        // Arrange
        val token = DefaultToken
        tokenRepository.saveToken(token)
        server.enqueue(MockResponse())
        val request = Request.Builder().url(server.url("/")).build()

        // Act
        client.newCall(request).execute()

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("Bearer ${token.accessToken}", recordedRequest.getHeader("Authorization"))
    }

    @Test
    fun doesNotAddAuthorizationHeader_whenTokenIsNull() {
        // Arrange
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
        val token = DefaultToken
        server.enqueue(MockResponse())
        val request = Request.Builder().url(server.url("/"))
            .header("Authorization", "Bearer ${DefaultToken.accessToken}")
            .build()

        // Act
        client.newCall(request).execute()

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("Bearer ${token.accessToken}", recordedRequest.getHeader("Authorization"))
    }

    @Test
    fun addsAuthorizationHeader_afterRefreshingExpiredToken() {
        // Arrange: repository with expired token and a refresh token available

        // We need a fresh client wired to this repository
        tokenRepository = TestTokenRepository(defaultToken = ExpiredToken).apply {
            tokenAfterRefresh = DefaultToken
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
        assertEquals(
            "Bearer ${DefaultToken.accessToken}",
            recordedRequest.getHeader("Authorization")
        )
    }

    @Test
    fun doesNotAddAuthorizationHeader_whenExpiredAndRefreshFails() {
        // Arrange: expired token but refresh fails
        tokenRepository = TestTokenRepository(defaultToken = ExpiredToken).apply {
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

    private companion object {
        val DefaultToken = AuthToken(
            accessToken = "access-token",
            refreshToken = "refresh-token",
            tokenType = "Bearer",
            expiresInSeconds = 3600,
            scopes = emptyList()
        )

        val ExpiredToken = DefaultToken.copy(
            accessToken = "expired-access-token",
            refreshToken = "expired-refresh-token",
            expiresAtMillis = System.currentTimeMillis() - 1
        )
    }
}