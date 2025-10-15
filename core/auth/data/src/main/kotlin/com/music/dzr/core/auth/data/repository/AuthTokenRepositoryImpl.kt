package com.music.dzr.core.auth.data.repository

import com.music.dzr.core.auth.data.local.model.AuthSession
import com.music.dzr.core.auth.data.local.model.authSession
import com.music.dzr.core.auth.data.local.source.AuthSessionLocalDataSource
import com.music.dzr.core.auth.data.local.source.AuthTokenLocalDataSource
import com.music.dzr.core.auth.data.mapper.toDomain
import com.music.dzr.core.auth.data.mapper.toLocal
import com.music.dzr.core.auth.data.remote.model.RedirectUriParams
import com.music.dzr.core.auth.data.remote.model.requireState
import com.music.dzr.core.auth.data.remote.oauth.AuthorizationUrlBuilder
import com.music.dzr.core.auth.data.remote.oauth.OAuthSecurityProvider
import com.music.dzr.core.auth.data.remote.oauth.parseRedirectUriParams
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
    private val tokenLocalDataSource: AuthTokenLocalDataSource,
    private val tokenRemoteDataSource: AuthTokenRemoteDataSource,
    private val sessionDataSource: AuthSessionLocalDataSource,
    private val dispatchers: DispatcherProvider,
    private val externalScope: ApplicationScope,
    private val clientId: String,
    private val redirectUri: String,
    private val authUrlBuilder: AuthorizationUrlBuilder,
    private val securityProvider: OAuthSecurityProvider
) : AuthTokenRepository {

    override suspend fun getToken(): Result<AuthToken, AppError> {
        return withContext(dispatchers.io) {
            when (val result = tokenLocalDataSource.get()) {
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
                    refreshToken = token.refreshToken ?: tokenLocalDataSource.get().run {
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

                when (val save = tokenLocalDataSource.save(mergedToken.toLocal())) {
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

                val tokenResponse = tokenRemoteDataSource.refreshToken(
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
                when (val clearResult = tokenLocalDataSource.clear()) {
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
                sessionDataSource.save(
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

    override suspend fun completeAuthorization(responseRedirectUri: String): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                val uriParams = responseRedirectUri.parseRedirectUriParams()
                if (uriParams is RedirectUriParams.Invalid) {
                    return@async Result.Failure(AuthError.InvalidRequest)
                }
                val session = getAndValidateSession(uriParams)

                when (session) {
                    is Result.Success -> handleAuthorizationResponse(uriParams, session.data)
                    is Result.Failure -> session
                }
            }.await()
        }
    }

    private suspend fun getAndValidateSession(
        redirectUriParams: RedirectUriParams
    ): Result<AuthSession, AppError> {
        val sessionResult = sessionDataSource.get()
        sessionDataSource.clear()

        if (sessionResult.isFailure()) {
            val error = when (sessionResult.error) {
                StorageError.NotFound -> AuthError.SessionExpired
                else -> sessionResult.error.toDomain()
            }
            return Result.Failure(error)
        }

        val session = sessionResult.data

        if (System.currentTimeMillis() - session.createdAtMillis > SessionTtlMillis) {
            return Result.Failure(AuthError.SessionExpired)
        }

        if (session.csrfState != redirectUriParams.requireState()) {
            return Result.Failure(AuthError.StateMismatch)
        }

        return Result.Success(session)
    }

    private suspend fun handleAuthorizationResponse(
        redirectUriParams: RedirectUriParams,
        session: AuthSession
    ): Result<Unit, AppError> {
        return when (redirectUriParams) {
            is RedirectUriParams.Error -> {
                val mappedError = redirectUriParams.toDomain()
                Result.Failure(mappedError)
            }

            is RedirectUriParams.Success -> {
                val tokenResponse = tokenRemoteDataSource.getToken(
                    code = redirectUriParams.code,
                    redirectUri = redirectUri,
                    clientId = clientId,
                    codeVerifier = session.codeVerifier
                )

                val newToken = tokenResponse.data
                if (tokenResponse.error == null && newToken != null) {
                    saveToken(newToken.toDomain())
                } else {
                    val mappedError = tokenResponse.error?.toDomain() ?: AuthError.Unexpected
                    mappedError.clearTokensIfInvalidGrant()
                    Result.Failure(mappedError)
                }
            }

            is RedirectUriParams.Invalid -> Result.Failure(AuthError.InvalidRequest)
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