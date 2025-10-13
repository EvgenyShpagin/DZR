package com.music.dzr.core.auth.data.remote.http

import com.music.dzr.core.auth.data.repository.TestTokenRepository
import com.music.dzr.core.auth.domain.error.AuthError
import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.model.AuthToken
import com.music.dzr.core.auth.domain.repository.getAccessToken
import com.music.dzr.core.auth.domain.repository.getRefreshToken
import com.music.dzr.core.error.ConnectivityError
import com.music.dzr.core.result.isFailure
import com.music.dzr.core.result.requireData
import kotlinx.coroutines.test.runTest
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthTokenAuthenticatorTest {

    private lateinit var tokenRepository: TestTokenRepository
    private lateinit var authenticator: AuthTokenAuthenticator

    private val mockRoute: Route? = null

    @BeforeTest
    fun setUp() {
        tokenRepository = TestTokenRepository()
        authenticator = AuthTokenAuthenticator(tokenRepository)
    }

    @Test
    fun returnsNewRequest_whenTokenRefreshIsSuccessful() = runTest {
        // Arrange
        val oldToken = "old_token"
        val newToken = AuthToken(
            accessToken = "new_access_token",
            refreshToken = "new_refresh_token",
            expiresInSeconds = 3600,
            scopes = listOf(AuthScope("scope")),
            tokenType = "bearer"
        )
        val mockResponse = mockResponseWithHeader("Bearer $oldToken")
        tokenRepository.setTokens(oldToken, "refresh_token")

        tokenRepository.tokenAfterRefresh = newToken

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNotNull(newRequest)
        assertEquals("Bearer ${newToken.accessToken}", newRequest.header("Authorization"))
        val accessToken = tokenRepository.getAccessToken()
        val refreshToken = tokenRepository.getRefreshToken()
        assertEquals(newToken.accessToken, accessToken.requireData())
        assertEquals(newToken.refreshToken, refreshToken.requireData())
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
    fun returnsNullAndClearsTokens_onInvalidGrant() = runTest {
        // Arrange
        val mockResponse = mockResponseWithHeader("Bearer old_token")
        tokenRepository.setTokens("old_token", "refresh_token")
        tokenRepository.forcedError = AuthError.InvalidGrant

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNull(newRequest)
        assertTrue(tokenRepository.getAccessToken().isFailure())
        assertTrue(tokenRepository.getRefreshToken().isFailure())
    }

    @Test
    fun doesNotRetryAndKeepsToken_whenRefreshFailsNonFatal() = runTest {
        // Arrange
        val mockResponse = mockResponseWithHeader("Bearer old_token")
        tokenRepository.setTokens("old_token", "refresh_token")
        tokenRepository.forcedError = ConnectivityError.HostUnreachable

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)
        tokenRepository.forcedError = null

        // Assert
        assertNull(newRequest)
        val accessToken = tokenRepository.getAccessToken()
        assertEquals("old_token", accessToken.requireData())
    }

    @Test
    fun returnsNull_whenNoTokensAtAll() = runTest {
        // Arrange
        val mockResponse = mockResponseWithHeader("Bearer any")
        tokenRepository.resetTokens()

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNull(newRequest)
        assertTrue(tokenRepository.getAccessToken().isFailure())
    }

    @Test
    fun returnsNull_whenAuthRetryLimitReached() = runTest {
        // Arrange: create a response with a prior 401 to simulate retry chain
        val mockResponse = mockResponseWithMaxPriorResponses()
        tokenRepository.setTokens("token", "refresh_token")

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert: guard triggers on >= 2 attempts
        assertNull(newRequest)
        val accessToken = tokenRepository.getAccessToken()
        assertEquals("token", accessToken.requireData())
    }

    private fun mockResponseWithMaxPriorResponses(): Response {
        val base = mockResponseWithHeader("Bearer token")
        var withPriors = base
        repeat(AuthTokenAuthenticator.MAX_RETRIES - 1) {
            withPriors = withPriors.newBuilder().priorResponse(base).build()
        }
        return withPriors
    }

    @Test
    fun retriesWithNewToken_ifWasRefreshedByAnotherThread() = runTest {
        // Arrange
        val failedRequestToken = "failed_token"
        val refreshedToken = "refreshed_token"
        val mockResponse = mockResponseWithHeader("Bearer $failedRequestToken")
        // New tokens which shouldn't be used
        tokenRepository.tokenAfterRefresh =
            TestTokenRepository.NonNullAuthToken.copy(accessToken = "will_not_be_used")
        // The imitation of updating the token in another thread
        tokenRepository.setTokens(refreshedToken, "any_refresh_token")

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNotNull(newRequest)
        assertEquals("Bearer $refreshedToken", newRequest.header("Authorization"))
        val token = tokenRepository.getToken()
        assertNotEquals(tokenRepository.tokenAfterRefresh, token.requireData())
    }

    private fun mockResponseWithHeader(authHeader: String): Response {
        val request = Request.Builder()
            .url("https://example.com")
            .header("Authorization", authHeader)
            .build()
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .build()
    }
}