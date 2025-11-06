package com.music.dzr.core.auth.data.repository

import com.music.dzr.core.auth.domain.error.AuthError
import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.model.AuthToken
import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
import com.music.dzr.core.data.test.HasForcedError
import com.music.dzr.core.data.test.runUnlessForcedError
import com.music.dzr.core.error.AppError
import com.music.dzr.core.error.PersistenceError
import com.music.dzr.core.result.Result
import com.music.dzr.core.storage.error.StorageError
import java.util.concurrent.atomic.AtomicReference

/**
 * A configurable in-memory test implementation of [AuthTokenRepository].
 *
 * This implementation allows for direct manipulation of the stored [AuthToken]
 * and can be configured to simulate error scenarios.
 */
class TestTokenRepository(
    defaultToken: AuthToken? = null
) : AuthTokenRepository, HasForcedError<AppError> {

    private val tokenStore = AtomicReference(defaultToken)

    override var forcedError: AppError? = null
    override var isStickyForcedError: Boolean = false

    var tokenAfterRefresh: AuthToken = tokenStore.get() ?: NonNullAuthToken

    override suspend fun getToken(): Result<AuthToken, AppError> = runUnlessForcedError {
        return when (val token = tokenStore.get()) {
            null -> Result.Failure(AuthError.NotAuthenticated)
            else -> Result.Success(token)
        }
    }

    override suspend fun saveToken(token: AuthToken): Result<Unit, AppError> =
        runUnlessForcedError {
            val currentToken = tokenStore.get()
            // According to the contract, we keep the existing refresh token if the new one is null
            val refreshTokenToSave = token.refreshToken ?: currentToken?.refreshToken
            tokenStore.set(token.copy(refreshToken = refreshTokenToSave))
            return Result.Success(Unit)
        }

    override suspend fun refreshToken(): Result<Unit, AppError> {
        forcedError?.let {
            if (it == AuthError.InvalidGrant || it is StorageError.DataCorrupted) {
                clearTokens()
            }
            return Result.Failure(it)
        }

        val currentToken = tokenStore.get()
        if (currentToken?.refreshToken == null) {
            return Result.Failure(AuthError.NotAuthenticated)
        }
        saveToken(tokenAfterRefresh)
        return Result.Success(Unit)
    }

    override suspend fun clearTokens(): Result<Unit, PersistenceError> = runUnlessForcedError {
        tokenStore.set(null)
        Result.Success(Unit)
    }

    override suspend fun initiateAuthorization(scopes: List<AuthScope>): Result<String, AppError> =
        runUnlessForcedError { return Result.Success("https://example.com/auth") }

    override suspend fun completeAuthorization(
        responseRedirectUri: String
    ): Result<Unit, AppError> = runUnlessForcedError {
        return Result.Success(Unit)
    }

    companion object {
        val NonNullAuthToken = AuthToken(
            accessToken = "access_token",
            tokenType = "Bearer",
            expiresInSeconds = 3600,
            refreshToken = null,
            scopes = emptyList()
        )
    }
}
