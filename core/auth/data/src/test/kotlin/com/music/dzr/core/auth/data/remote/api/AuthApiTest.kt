package com.music.dzr.core.auth.data.remote.api

import com.music.dzr.core.network.test.createApi
import com.music.dzr.core.network.test.enqueueResponseFromAssets
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import java.net.URLDecoder
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AuthApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: AuthApi

    @BeforeTest
    fun setUp() {
        server = MockWebServer()
        server.start()
        api = createApi(server.url("/"))
    }

    @AfterTest
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getToken_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("refresh-token.json")

        // Act
        val response = api.getToken(
            code = "test",
            redirectUri = "test",
            clientId = "test",
            codeVerifier = "test"
        )

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
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
        server.enqueueResponseFromAssets("refresh-token.json")
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
        server.enqueueResponseFromAssets("refresh-token.json")

        // Act
        val response = api.refreshToken(
            refreshToken = "test",
            clientId = "test"
        )

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
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
        server.enqueueResponseFromAssets("refresh-token.json")
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