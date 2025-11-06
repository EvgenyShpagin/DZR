package com.music.dzr.core.auth.data.remote.http

import com.music.dzr.core.auth.data.repository.TestTokenRepository
import com.music.dzr.core.auth.domain.error.AuthError
import com.music.dzr.core.auth.domain.model.AuthToken
import com.music.dzr.core.auth.domain.repository.getAccessToken
import com.music.dzr.core.auth.domain.repository.getRefreshToken
import com.music.dzr.core.error.ConnectivityError
import com.music.dzr.core.testing.assertion.assertFailure
import com.music.dzr.core.testing.assertion.assertSuccessEquals
import com.music.dzr.core.testing.assertion.assertSuccessNotEquals
import kotlinx.coroutines.test.runTest
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

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
        val newToken = DefaultToken
        val oldToken = ExpiredToken
        val mockResponse = mockResponseWithHeader("Bearer ${oldToken.accessToken}")
        tokenRepository.saveToken(oldToken)
        tokenRepository.tokenAfterRefresh = newToken

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNotNull(newRequest)
        assertEquals("Bearer ${newToken.accessToken}", newRequest.header("Authorization"))
        assertSuccessEquals(
            expectedData = newToken.accessToken,
            actual = tokenRepository.getAccessToken()
        )
        assertSuccessEquals(
            expectedData = newToken.refreshToken,
            actual = tokenRepository.getRefreshToken()
        )
    }

    @Test
    fun returnsNull_whenRefreshTokenIsMissing() = runTest {
        // Arrange
        val tokenWithoutRefresh = ExpiredToken.copy(refreshToken = null)
        val mockResponse = mockResponseWithHeader("Bearer ${tokenWithoutRefresh.accessToken}")
        tokenRepository.saveToken(tokenWithoutRefresh)

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNull(newRequest)
    }

    @Test
    fun returnsNullAndClearsTokens_onInvalidGrant() = runTest {
        // Arrange
        val token = ExpiredToken
        val mockResponse = mockResponseWithHeader("Bearer ${token.accessToken}")
        tokenRepository.saveToken(token)
        tokenRepository.forcedError = AuthError.InvalidGrant
        tokenRepository.isStickyForcedError = true

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNull(newRequest)
        assertFailure(tokenRepository.getAccessToken())
        assertFailure(tokenRepository.getRefreshToken())
    }

    @Test
    fun doesNotRetryAndKeepsToken_whenRefreshFailsNonFatal() = runTest {
        // Arrange
        val token = DefaultToken
        val mockResponse = mockResponseWithHeader("Bearer any")
        tokenRepository.saveToken(token)
        tokenRepository.forcedError = ConnectivityError.HostUnreachable

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)
        tokenRepository.forcedError = null

        // Assert
        assertNull(newRequest)
        val accessToken = tokenRepository.getAccessToken()
        assertSuccessEquals(token.accessToken, accessToken)
    }

    @Test
    fun returnsNull_whenNoTokensAtAll() = runTest {
        // Arrange
        val mockResponse = mockResponseWithHeader("Bearer any")

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNull(newRequest)
        assertFailure(tokenRepository.getAccessToken())
    }

    @Test
    fun returnsNull_whenAuthRetryLimitReached() = runTest {
        // Arrange: create a response with a prior 401 to simulate retry chain
        val token = DefaultToken
        val mockResponse = mockResponseWithMaxPriorResponses(token.accessToken)
        tokenRepository.saveToken(token)

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert: guard triggers on >= 2 attempts
        assertNull(newRequest)
        val accessToken = tokenRepository.getAccessToken()
        assertSuccessEquals(token.accessToken, accessToken)
    }

    private fun mockResponseWithMaxPriorResponses(accessToken: String): Response {
        val base = mockResponseWithHeader("Bearer $accessToken")
        var withPriors = base
        repeat(AuthTokenAuthenticator.MAX_RETRIES - 1) {
            withPriors = withPriors.newBuilder().priorResponse(base).build()
        }
        return withPriors
    }

    @Test
    fun retriesWithNewToken_ifWasRefreshedByAnotherThread() = runTest {
        // Arrange
        val failedRequestToken = DefaultToken.copy(accessToken = "failed")
        val refreshedToken = DefaultToken
        val mockResponse = mockResponseWithHeader("Bearer ${failedRequestToken.accessToken}")
        // New tokens which shouldn't be used
        tokenRepository.tokenAfterRefresh = DefaultToken.copy(accessToken = "will_not_be_used")
        // The imitation of updating the token in another thread
        tokenRepository.saveToken(refreshedToken)

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNotNull(newRequest)
        assertEquals(
            expected = "Bearer ${refreshedToken.accessToken}",
            actual = newRequest.header("Authorization")
        )
        assertSuccessNotEquals(
            illegal = tokenRepository.tokenAfterRefresh,
            actual = tokenRepository.getToken()
        )
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
