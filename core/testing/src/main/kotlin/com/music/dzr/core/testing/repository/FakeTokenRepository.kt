package com.music.dzr.core.testing.repository

import com.music.dzr.core.auth.domain.model.AuthToken
import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
import java.util.concurrent.atomic.AtomicReference

class FakeTokenRepository(
    defaultToken: AuthToken? = null
) : AuthTokenRepository {

    private val tokenStore = AtomicReference(defaultToken)

    /**
     * Determines whether the token refresh simulation will succeed.
     * If `true`, [refreshToken] will return `true` and refresh the token
     */
    var refreshShouldSucceed = true

    /**
     * If `true`, then if a token refresh fails, all tokens will be deleted.
     * This simulates a non-recoverable error such as "invalid_grant".
     */
    var clearTokensOnRefreshFailure = true

    fun resetTokens() {
        tokenStore.set(null)
    }

    fun setTokens(accessToken: String, refreshToken: String?) {
        val oldToken = tokenStore.get() ?: NonNullAuthToken
        val newToken = oldToken.copy(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
        tokenStore.set(newToken)
    }

    var tokenAfterRefresh: AuthToken = tokenStore.get() ?: NonNullAuthToken

    override suspend fun getToken(): AuthToken? = tokenStore.get()

    override suspend fun saveToken(token: AuthToken) {
        val currentToken = tokenStore.get()
        // According to the contract, we keep the existing refresh token if the new one is null
        val refreshTokenToSave = token.refreshToken ?: currentToken?.refreshToken
        tokenStore.set(token.copy(refreshToken = refreshTokenToSave))
    }

    override suspend fun refreshToken(): Boolean {
        val currentToken = tokenStore.get()
        if (currentToken?.refreshToken == null) {
            return false
        }

        return if (refreshShouldSucceed) {
            saveToken(tokenAfterRefresh)
            true
        } else {
            if (clearTokensOnRefreshFailure) {
                clearTokens()
            }
            false
        }
    }

    override suspend fun clearTokens() {
        tokenStore.set(null)
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