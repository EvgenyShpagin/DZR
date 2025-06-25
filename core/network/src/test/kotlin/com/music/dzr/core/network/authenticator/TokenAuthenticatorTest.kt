package com.music.dzr.core.network.authenticator

import com.music.dzr.core.network.api.AuthApi
import com.music.dzr.core.network.model.auth.Token
import com.music.dzr.core.network.model.error.NetworkError
import com.music.dzr.core.network.model.error.NetworkErrorType
import com.music.dzr.core.network.model.shared.NetworkResponse
import com.music.dzr.core.testing.repository.FakeTokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class TokenAuthenticatorTest {

    private lateinit var tokenRepository: FakeTokenRepository
    private val authApi: AuthApi = mockk()
    private lateinit var authenticator: TokenAuthenticator

    private val mockRoute: Route? = null

    @Before
    fun setUp() {
        tokenRepository = FakeTokenRepository()
        authenticator = TokenAuthenticator(tokenRepository, "test_client_id", authApi)
    }

    @Test
    fun returnsNewRequest_whenTokenRefreshIsSuccessful() = runTest {
        // Arrange
        val oldToken = "old_token"
        val newToken = Token(
            accessToken = "new_access_token",
            refreshToken = "new_refresh_token",
            expiresIn = 3600,
            scope = "scope",
            tokenType = "bearer"
        )
        val mockResponse = mockResponseWithHeader("Bearer $oldToken")
        tokenRepository.setTokens(oldToken, "refresh_token")
        coEvery { authApi.refreshToken(any(), any(), any()) } returns NetworkResponse(newToken)

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNotNull(newRequest)
        assertEquals("Bearer ${newToken.accessToken}", newRequest?.header("Authorization"))
        assertEquals(newToken.accessToken, tokenRepository.getAccessToken())
        assertEquals(newToken.refreshToken, tokenRepository.getRefreshToken())
    }

    @Test
    fun returnsNull_whenRefreshTokenIsMissing() = runTest {
        // Arrange
        val mockResponse = mockResponseWithHeader("Bearer old_token")
        tokenRepository.setTokens("old_token", null)

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNull(newRequest)
    }

    @Test
    fun returnsNullAndClearsTokens_onInvalidGrantError() = runTest {
        // Arrange
        val mockResponse = mockResponseWithHeader("Bearer old_token")
        val error = NetworkError(
            type = NetworkErrorType.HttpException,
            message = "Invalid Grant",
            code = 400,
            reason = "invalid_grant"
        )
        tokenRepository.setTokens("old_token", "refresh_token")
        coEvery { authApi.refreshToken(any(), any(), any()) } returns NetworkResponse(null, error)

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNull(newRequest)
        assertNull(tokenRepository.getAccessToken())
        assertNull(tokenRepository.getRefreshToken())
    }

    @Test
    fun returnsNullAndKeepsSession_onTransientRefreshError() = runTest {
        // Arrange
        val mockResponse = mockResponseWithHeader("Bearer old_token")
        val error = NetworkError(
            type = NetworkErrorType.Timeout,
            message = "timeout",
            code = 500,
            reason = "timeout"
        )
        tokenRepository.setTokens("old_token", "refresh_token")
        coEvery { authApi.refreshToken(any(), any(), any()) } returns
                NetworkResponse(null, error)

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNull(newRequest)
        assertEquals("old_token", tokenRepository.getAccessToken())
    }

    @Test
    fun retriesWithNewToken_ifWasRefreshedByAnotherThread() = runTest {
        // Arrange
        val failedRequestToken = "failed_token"
        val refreshedToken = "refreshed_token"
        val mockResponse = mockResponseWithHeader("Bearer $failedRequestToken")
        tokenRepository.setTokens(refreshedToken, "any_refresh_token")

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNotNull(newRequest)
        assertEquals("Bearer $refreshedToken", newRequest?.header("Authorization"))
        coVerify(exactly = 0) { authApi.refreshToken(any(), any(), any()) }
    }

    private fun mockResponseWithHeader(authHeader: String): Response {
        val request = Request.Builder()
            .url("https://example.com")
            .header("Authorization", authHeader)
            .build()
        return Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .build()
    }
}