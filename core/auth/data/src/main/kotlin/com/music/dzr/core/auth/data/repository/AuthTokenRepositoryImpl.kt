package com.music.dzr.core.auth.data.repository

import com.music.dzr.core.auth.data.local.model.authSession
import com.music.dzr.core.auth.data.local.source.AuthSessionLocalDataSource
import com.music.dzr.core.auth.data.local.source.AuthTokenLocalDataSource
import com.music.dzr.core.auth.data.mapper.toDomain
import com.music.dzr.core.auth.data.mapper.toLocal
import com.music.dzr.core.auth.data.remote.oauth.AuthorizationUrlBuilder
import com.music.dzr.core.auth.data.remote.oauth.OAuthSecurityProvider
import com.music.dzr.core.auth.data.remote.source.AuthTokenRemoteDataSource
import com.music.dzr.core.auth.domain.error.AuthError
import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.model.AuthToken
import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
import com.music.dzr.core.auth.domain.repository.getRefreshToken
import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.coroutine.DispatcherProvider
import com.music.dzr.core.error.AppError
import com.music.dzr.core.error.PersistenceError
import com.music.dzr.core.result.Result
import com.music.dzr.core.result.isFailure
import com.music.dzr.core.result.onFailure
import com.music.dzr.core.storage.error.StorageError
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes

internal class AuthTokenRepositoryImpl(
    private val localDataSource: AuthTokenLocalDataSource,
    private val authSessionDataSource: AuthSessionLocalDataSource,
    private val remoteDataSource: AuthTokenRemoteDataSource,
    private val dispatchers: DispatcherProvider,
    private val externalScope: ApplicationScope,
    private val clientId: String,
    private val redirectUri: String,
    private val authUrlBuilder: AuthorizationUrlBuilder,
    private val securityProvider: OAuthSecurityProvider
) : AuthTokenRepository {

    override suspend fun getToken(): Result<AuthToken, AppError> {
        return withContext(dispatchers.io) {
            when (val result = localDataSource.get()) {
                is Result.Success -> Result.Success(result.data.toDomain())
                is Result.Failure -> Result.Failure(
                    result.error
                        .also { it.clearTokensIfDataCorrupted() }
                        .toDomain()
                )
            }
        }
    }

    override suspend fun saveToken(token: AuthToken): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                // Preserve existing refresh token if the new one is absent
                val mergedToken = token.copy(
                    refreshToken = token.refreshToken ?: localDataSource.get().run {
                        when (this) {
                            is Result.Success -> data.takeIf { it.hasRefreshToken() }?.refreshToken
                            is Result.Failure -> when (error) {
                                StorageError.NotFound -> null
                                else -> return@async Result.Failure(
                                    error
                                        .also { it.clearTokensIfDataCorrupted() }
                                        .toDomain()
                                )
                            }
                        }
                    }
                )

                when (val save = localDataSource.save(mergedToken.toLocal())) {
                    is Result.Success -> Result.Success(Unit)
                    is Result.Failure -> Result.Failure(
                        save.error
                            .also { it.clearTokensIfDataCorrupted() }
                            .toDomain()
                    )
                }
            }.await()
        }
    }

    override suspend fun refreshToken(): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                val refreshTokenResult = getRefreshToken()

                if (refreshTokenResult.isFailure()) {
                    return@async refreshTokenResult
                }
                val refreshToken = refreshTokenResult.data
                    ?: return@async Result.Failure(AuthError.NotAuthenticated)

                val tokenResponse = remoteDataSource.refreshToken(
                    refreshToken = refreshToken,
                    clientId = clientId
                )

                val newToken = tokenResponse.data
                if (tokenResponse.error == null && newToken != null) {
                    when (val saveResult = saveToken(newToken.toDomain())) {
                        is Result.Success -> Result.Success(Unit)
                        is Result.Failure -> saveResult
                    }
                } else {
                    val mapped = tokenResponse.error?.toDomain()
                        ?: AuthError.Unexpected
                    mapped.clearTokensIfInvalidGrant()
                    Result.Failure(mapped)
                }
            }.await()
        }
    }

    override suspend fun clearTokens(): Result<Unit, PersistenceError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                when (val clearResult = localDataSource.clear()) {
                    is Result.Success -> Result.Success(Unit)
                    is Result.Failure -> {
                        if (clearResult.error == StorageError.NotFound) Result.Success(Unit)
                        else Result.Failure(clearResult.error.toDomain() as PersistenceError)
                    }
                }
            }.await()
        }
    }

    override suspend fun initiateAuthorization(scopes: List<AuthScope>): Result<String, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                val codeVerifier = securityProvider.generateCodeVerifier()
                val codeChallenge = securityProvider.deriveCodeChallengeS256(codeVerifier)
                val state = securityProvider.generateCsrfState()

                // Persist ephemeral session (PKCE/state)
                authSessionDataSource.save(
                    authSession {
                        this.codeVerifier = codeVerifier
                        this.csrfState = state
                        this.createdAtMillis = System.currentTimeMillis()
                    }
                ).onFailure { error ->
                    return@async Result.Failure(error.toDomain())
                }

                val url = authUrlBuilder.build(
                    redirectUri = redirectUri,
                    scopes = scopes,
                    state = state,
                    codeChallenge = codeChallenge
                )
                Result.Success(url)
            }.await()
        }
    }

    /**
     * Clears tokens on an unrecoverable `invalid_grant` error.
     * This error indicates that the authorization grant (e.g., authorization code or refresh token)
     * is invalid, expired, or revoked.
     */
    private suspend fun AppError.clearTokensIfInvalidGrant() {
        if (this == AuthError.InvalidGrant) clearTokens()
    }

    /**
     * Clears tokens on a `DataCorrupted` error.
     * This error indicates that the data stored in the local storage is corrupted.
     */
    private suspend fun AppError.clearTokensIfDataCorrupted() {
        if (this is StorageError.DataCorrupted) clearTokens()
    }

    private companion object {
        val SessionTtlMillis = 10.minutes.inWholeMilliseconds
    }
}