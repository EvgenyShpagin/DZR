package com.music.dzr.core.auth.data.remote.http

import com.music.dzr.core.testing.repository.FakeTokenRepository
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
    private lateinit var tokenRepository: FakeTokenRepository

    @BeforeTest
    fun setUp() {
        server = MockWebServer()
        server.start()
        tokenRepository = FakeTokenRepository()
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
}