package com.music.dzr.core.auth.data.repository

import com.music.dzr.core.auth.data.local.error.SecureStorageError
import com.music.dzr.core.auth.data.local.model.authSession
import com.music.dzr.core.auth.data.local.model.copy
import com.music.dzr.core.auth.data.local.source.TestAuthSessionLocalDataSource
import com.music.dzr.core.auth.data.local.source.TestAuthTokenLocalDataSource
import com.music.dzr.core.auth.data.mapper.toLocal
import com.music.dzr.core.auth.data.mapper.toNetwork
import com.music.dzr.core.auth.data.remote.oauth.AuthorizationUrlBuilder
import com.music.dzr.core.auth.data.remote.oauth.OAuthSecurityProvider
import com.music.dzr.core.auth.data.remote.oauth.OAuthSecurityProviderImpl
import com.music.dzr.core.auth.data.remote.source.TestAuthTokenRemoteDataSource
import com.music.dzr.core.auth.domain.error.AuthError
import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.model.AuthToken
import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.error.PersistenceError
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.network.dto.error.NetworkErrorType
import com.music.dzr.core.storage.error.StorageError
import com.music.dzr.core.testing.assertion.assertFailure
import com.music.dzr.core.testing.assertion.assertFailureEquals
import com.music.dzr.core.testing.assertion.assertFailureIs
import com.music.dzr.core.testing.assertion.assertSuccess
import com.music.dzr.core.testing.coroutine.TestDispatcherProvider
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import okio.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTokenRepositoryImplTest {

    private val clientId: String = "test-client-id"
    private val redirectUri: String = "test-redirect-uri"
    private val authBaseUrl: String = "https://test-auth-base.com/"

    private lateinit var repository: AuthTokenRepository
    private lateinit var tokenLocalDataSource: TestAuthTokenLocalDataSource
    private lateinit var tokenRemoteDataSource: TestAuthTokenRemoteDataSource
    private lateinit var sessionDataSource: TestAuthSessionLocalDataSource
    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatchers: TestDispatcherProvider
    private lateinit var externalScope: ApplicationScope
    private lateinit var authUrlBuilder: AuthorizationUrlBuilder
    private lateinit var securityProvider: OAuthSecurityProvider

    @BeforeTest
    fun setUp() {
        // Test implementations
        tokenLocalDataSource = TestAuthTokenLocalDataSource()
        tokenRemoteDataSource = TestAuthTokenRemoteDataSource()
        sessionDataSource = TestAuthSessionLocalDataSource()
        scheduler = TestCoroutineScheduler()
        dispatchers = TestDispatcherProvider(scheduler)
        // Real implementations
        externalScope = ApplicationScope(dispatchers)
        authUrlBuilder = AuthorizationUrlBuilder(clientId, authBaseUrl)
        securityProvider = OAuthSecurityProviderImpl()

        repository = AuthTokenRepositoryImpl(
            tokenLocalDataSource = tokenLocalDataSource,
            tokenRemoteDataSource = tokenRemoteDataSource,
            sessionDataSource = sessionDataSource,
            dispatchers = dispatchers,
            externalScope = externalScope,
            clientId = clientId,
            redirectUri = redirectUri,
            authUrlBuilder = authUrlBuilder,
            securityProvider = securityProvider,
        )
    }

    @Test
    fun getToken_returnsFailure_whenTokenIsNotStored() = runTest(scheduler) {
        // Act
        val result = repository.getToken()
        // Assert
        assertFailure(result)
    }

    @Test
    fun getToken_returnsFailureAndClearsTokens_whenDataCorrupted() = runTest(scheduler) {
        // Arrange
        tokenLocalDataSource.save(DefaultLocalAuthToken)
        tokenLocalDataSource.forcedError = StorageError.DataCorrupted(IOException())
        // Act
        val result = repository.getToken()
        // Assert
        assertFailure(result)
        val storedToken = tokenLocalDataSource.get()
        assertFailureEquals(StorageError.NotFound, storedToken)
    }

    @Test
    fun getToken_returnsSuccess_whenTokenIsStored() = runTest(scheduler) {
        // Arrange
        tokenLocalDataSource.save(DefaultLocalAuthToken)
        // Act
        val result = repository.getToken()
        // Assert
        assertSuccess(result)
    }

    @Test
    fun saveToken_returnsFailureAndClearsTokens_whenDataCorrupted() = runTest(scheduler) {
        // Arrange
        val token = DefaultAuthToken
        tokenLocalDataSource.forcedError = StorageError.DataCorrupted(IOException())
        // Act
        val result = repository.saveToken(token)
        // Assert
        assertFailure(result)
        val persistedToken = tokenLocalDataSource.get()
        assertFailureEquals(StorageError.NotFound, persistedToken)
    }

    @Test
    fun saveToken_returnsSuccessAndPreservesExistingRefreshToken_whenNewIsAbsent() =
        runTest(scheduler) {
            // Arrange
            val savedTokenWithRefresh = DefaultLocalAuthToken
            tokenLocalDataSource.save(savedTokenWithRefresh)
            val newTokenWithoutRefresh = DefaultAuthToken.copy(refreshToken = null)
            // Act
            val result = repository.saveToken(newTokenWithoutRefresh)
            // Assert
            assertSuccess(result)
            val savedToken = tokenLocalDataSource.get()
            assertSuccess(savedToken)
            assertEquals(savedToken.data.refreshToken, savedTokenWithRefresh.refreshToken)
        }

    @Test
    fun saveToken_returnsSuccess_whenTokenIsSaved() = runTest(scheduler) {
        // Arrange
        val token = DefaultAuthToken
        // Act
        val result = repository.saveToken(token)
        // Assert
        assertSuccess(result)
    }

    @Test
    fun refreshToken_returnsFailure_whenReadingLocalTokenFails() = runTest(scheduler) {
        // Arrange
        tokenLocalDataSource.forcedError = StorageError.ReadFailed(IOException())
        // Act
        val result = repository.refreshToken()
        // Assert
        assertFailure(result)
    }

    @Test
    fun refreshToken_returnsNotAuthenticated_whenTokenIsNotStored() = runTest(scheduler) {
        // Arrange
        tokenLocalDataSource.forcedError = StorageError.NotFound
        // Act
        val result = repository.refreshToken()
        // Assert
        assertFailureEquals(AuthError.NotAuthenticated, result)
    }

    @Test
    fun refreshToken_returnsFailure_whenRefreshTokenRequestFailed() = runTest(scheduler) {
        // Arrange
        tokenLocalDataSource.save(DefaultLocalAuthToken)
        tokenRemoteDataSource.forcedError = SomeNetworkError
        // Act
        val result = repository.refreshToken()
        // Assert
        assertFailure(result)
    }

    @Test
    fun refreshToken_returnsFailureAndClearsTokens_whenInvalidGrant() = runTest(scheduler) {
        // Arrange
        tokenLocalDataSource.save(DefaultLocalAuthToken)
        tokenRemoteDataSource.forcedError = InvalidGrantNetworkError
        // Act
        val result = repository.refreshToken()
        // Assert
        assertFailureEquals(AuthError.InvalidGrant, result)
        val persistedToken = tokenLocalDataSource.get()
        assertFailureEquals(StorageError.NotFound, persistedToken)
    }

    @Test
    fun refreshToken_returnsSuccess_whenRefreshed() = runTest(scheduler) {
        // Arrange
        tokenLocalDataSource.save(DefaultLocalAuthToken)
        tokenRemoteDataSource.exchangedToken = DefaultNetworkToken
        // Act
        val result = repository.refreshToken()
        // Assert
        assertSuccess(result)
    }

    @Test
    fun clearTokens_returnsFailure_whenPersistenceError() = runTest(scheduler) {
        // Arrange
        tokenLocalDataSource.forcedError = StorageError.WriteFailed(IOException())
        // Act
        val result = repository.clearTokens()
        // Assert
        assertFailure(result)
    }

    @Test
    fun clearTokens_returnsSuccess_whenTokensAreCleared() = runTest(scheduler) {
        // Arrange
        tokenLocalDataSource.save(DefaultLocalAuthToken)
        // Act
        val result = repository.clearTokens()
        // Assert
        assertSuccess(result)
        val persistedToken = tokenLocalDataSource.get()
        assertFailureEquals(StorageError.NotFound, persistedToken)
    }

    @Test
    fun initiateAuthorization_returnsFailure_whenSessionDataSaveFailed() = runTest(scheduler) {
        // Arrange
        val scopes = listOf(AuthScope("user-read-email"))
        sessionDataSource.forcedError = SecureStorageError.DecryptionFailed(IOException())
        // Act
        val result = repository.initiateAuthorization(scopes)
        // Assert
        assertFailureIs<PersistenceError>(result)
    }

    @Test
    fun initiateAuthorization_returnsSuccess_whenSessionDataSaved() = runTest(scheduler) {
        // Arrange
        val scopes = listOf(AuthScope("user-read-email"))
        // Act
        val result = repository.initiateAuthorization(scopes)
        // Assert
        assertSuccess(result)
    }

    @Test
    fun completeAuthorization_returnsFailure_whenInvalidRedirectUri() = runTest(scheduler) {
        // Arrange
        sessionDataSource.save(DefaultSession)
        val responseRedirectUri = "this is not a valid uri"
        // Act
        val result = repository.completeAuthorization(responseRedirectUri)
        // Assert
        assertFailure(result)
    }

    @Test
    fun completeAuthorization_returnsSessionExpired_whenOldTokenStored() = runTest(scheduler) {
        // Arrange
        val oldSession = DefaultSession.copy { this.createdAtMillis = 0 }
        sessionDataSource.save(oldSession)
        val responseRedirectUri = DEFAULT_REDIRECT_URI
        // Act
        val result = repository.completeAuthorization(responseRedirectUri)
        // Assert
        assertFailureIs<AuthError.SessionExpired>(result)
    }

    @Test
    fun completeAuthorization_returnsStateMismatch_whenCsrfStateDiffers() = runTest(scheduler) {
        // Arrange
        val responseRedirectUri = DEFAULT_REDIRECT_URI
        val session = DefaultSession.copy { this.csrfState = "OTHER_STATE" }
        sessionDataSource.save(session)
        // Act
        val result = repository.completeAuthorization(responseRedirectUri)
        // Assert
        assertFailureIs<AuthError.StateMismatch>(result)
    }

    @Test
    fun completeAuthorization_returnsFailure_whenErrorRedirectUri() = runTest(scheduler) {
        // Arrange
        sessionDataSource.save(DefaultSession)
        val responseRedirectUri = ErrorRedirectUri
        // Act
        val result = repository.completeAuthorization(responseRedirectUri)
        // Assert
        assertFailure(result)
    }

    @Test
    fun completeAuthorization_returnsFailure_whenTokenExchangeFailed() = runTest(scheduler) {
        // Arrange
        sessionDataSource.save(DefaultSession)
        val responseRedirectUri = DEFAULT_REDIRECT_URI
        tokenRemoteDataSource.forcedError = SomeNetworkError
        // Act
        val result = repository.completeAuthorization(responseRedirectUri)
        // Assert
        assertFailure(result)
    }

    @Test
    fun completeAuthorization_returnsFailureAndClearsTokens_whenInvalidGrant() =
        runTest(scheduler) {
            // Arrange
            val responseRedirectUri = DEFAULT_REDIRECT_URI
            sessionDataSource.save(DefaultSession)
            tokenRemoteDataSource.forcedError = InvalidGrantNetworkError
            // Act
            val result = repository.completeAuthorization(responseRedirectUri)
            // Assert
            assertFailureIs<AuthError.InvalidGrant>(result)
            val persistedToken = tokenLocalDataSource.get()
            assertFailureEquals(StorageError.NotFound, persistedToken)
        }

    @Test
    fun completeAuthorization_returnsSuccess_whenAuthorizationCompleted() = runTest(scheduler) {
        // Arrange
        sessionDataSource.save(DefaultSession)
        val responseRedirectUri = DEFAULT_REDIRECT_URI
        // Act
        val result = repository.completeAuthorization(responseRedirectUri)
        // Assert
        assertSuccess(result)
        assertSuccess(tokenLocalDataSource.get())
    }

    companion object {

        private val DefaultAuthToken = AuthToken(
            accessToken = "ACCESS_TOKEN",
            refreshToken = "REFRESH_TOKEN",
            expiresInSeconds = 3600,
            scopes = listOf(AuthScope("user-read-email")),
            tokenType = "Bearer",
            expiresAtMillis = System.currentTimeMillis() + 3600 * 1000
        )
        private val DefaultLocalAuthToken = DefaultAuthToken.toLocal()
        private val DefaultNetworkToken = DefaultAuthToken.toNetwork()

        private val SomeNetworkError = NetworkError(
            type = NetworkErrorType.SomeConnectionError,
            message = "",
            code = 0
        )

        private val InvalidGrantNetworkError = NetworkError(
            type = NetworkErrorType.HttpException,
            message = "",
            code = 401,
            reason = "invalid_grant"
        )

        private val DefaultSession
            get() = authSession {
                this.csrfState = "STATE_XYZ"
                this.createdAtMillis = System.currentTimeMillis()
                this.codeVerifier = "code-verififer"
            }

        private val DEFAULT_REDIRECT_URI =
            "https://example.com/callback?code=AUTH_CODE&state=${DefaultSession.csrfState}"

        private val ErrorRedirectUri = "https://example.com/callback?error=access_denied&" +
                "state=${DefaultSession.csrfState}&error_description=User%20denied%20access"
    }
}
