package com.music.dzr.core.network.api

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.net.URLDecoder

class AuthApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: AuthApi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        api = createApi(server.url("/"))
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getToken_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/auth/refresh-token.json")

        // Act
        val response = api.getToken(
            code = "test",
            redirectUri = "test",
            clientId = "test",
            codeVerifier = "test"
        )

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals("BQBLuPRYBQ...BP8stIv5xr-Iwaf4l8eg", accessToken)
            assertEquals("Bearer", tokenType)
            assertEquals(3600, expiresIn)
            assertEquals("AQAQfyEFmJJuCvAFh...cG_m-2KTgNDaDMQqjrOa3", refreshToken)
            assertEquals("user-read-email user-read-private", scope)
        }
    }

    @Test
    fun getToken_usesCorrectPathMethodAndBody() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/auth/refresh-token.json")
        val expectedBody = "grant_type=authorization_code" +
                "&code=test_code" +
                "&redirect_uri=test_uri" +
                "&client_id=test_client_id" +
                "&code_verifier=test_verifier"

        // Act
        api.getToken(
            code = "test_code",
            redirectUri = "test_uri",
            clientId = "test_client_id",
            codeVerifier = "test_verifier"
        )

        // Assert
        val request = server.takeRequest()
        assertEquals("/api/token", request.path)
        assertEquals("POST", request.method)

        val actualBody = URLDecoder.decode(request.body.readUtf8(), "UTF-8")
        assertEquals(expectedBody, actualBody)
    }


    @Test
    fun refreshToken_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/auth/refresh-token.json")

        // Act
        val response = api.refreshToken(
            refreshToken = "test",
            clientId = "test"
        )

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals("BQBLuPRYBQ...BP8stIv5xr-Iwaf4l8eg", accessToken)
            assertEquals("Bearer", tokenType)
            assertEquals(3600, expiresIn)
            assertEquals("AQAQfyEFmJJuCvAFh...cG_m-2KTgNDaDMQqjrOa3", refreshToken)
            assertEquals("user-read-email user-read-private", scope)
        }
    }

    @Test
    fun refreshToken_usesCorrectPathMethodAndBody() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/auth/refresh-token.json")
        val expectedBody = "grant_type=refresh_token" +
                "&refresh_token=refresh_token" +
                "&client_id=client_id"

        // Act
        api.refreshToken(
            refreshToken = "refresh_token",
            clientId = "client_id"
        )

        // Assert
        val request = server.takeRequest()
        assertEquals("/api/token", request.path)
        assertEquals("POST", request.method)

        val actualBody = URLDecoder.decode(request.body.readUtf8(), "UTF-8")
        assertEquals(expectedBody, actualBody)
    }
} 