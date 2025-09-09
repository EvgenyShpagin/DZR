package com.music.dzr.core.auth.data.remote.http

import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.model.AuthToken
import com.music.dzr.core.auth.domain.util.getAccessToken
import com.music.dzr.core.auth.domain.util.getRefreshToken
import com.music.dzr.core.testing.repository.FakeTokenRepository
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

class AuthTokenAuthenticatorTest {

    private lateinit var tokenRepository: FakeTokenRepository
    private lateinit var authenticator: AuthTokenAuthenticator

    private val mockRoute: Route? = null

    @BeforeTest
    fun setUp() {
        tokenRepository = FakeTokenRepository()
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

        tokenRepository.refreshShouldSucceed = true
        tokenRepository.tokenAfterRefresh = newToken

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNotNull(newRequest)
        assertEquals("Bearer ${newToken.accessToken}", newRequest.header("Authorization"))
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
        tokenRepository.setTokens("old_token", "refresh_token")
        tokenRepository.refreshShouldSucceed = false

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
        tokenRepository.setTokens("old_token", "refresh_token")
        tokenRepository.clearTokensOnRefreshFailure = false
        tokenRepository.refreshShouldSucceed = false

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
        // New tokens which shouldn't be used
        tokenRepository.refreshShouldSucceed = true
        tokenRepository.tokenAfterRefresh =
            FakeTokenRepository.Companion.NonNullAuthToken.copy(accessToken = "will_not_be_used")
        // The imitation of updating the token in another thread
        tokenRepository.setTokens(refreshedToken, "any_refresh_token")

        // Act
        val newRequest = authenticator.authenticate(mockRoute, mockResponse)

        // Assert
        assertNotNull(newRequest)
        assertEquals("Bearer $refreshedToken", newRequest.header("Authorization"))
        assertNotEquals(tokenRepository.tokenAfterRefresh, tokenRepository.getToken())
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