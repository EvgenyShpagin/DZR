package com.music.dzr.core.testing.repository

import com.music.dzr.core.oauth.model.Token
import com.music.dzr.core.oauth.repository.TokenRepository

class FakeTokenRepository : TokenRepository {

    private var accessToken: String? = null
    private var refreshToken: String? = null
    private var tokenType: String? = "Bearer"

    fun setTokens(accessToken: String?, refreshToken: String?) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    override suspend fun saveToken(token: Token) {
        this.accessToken = token.accessToken
        // Per the interface docs, preserve refresh token if the new one is null
        if (token.refreshToken != null) {
            this.refreshToken = token.refreshToken
        }
        this.tokenType = token.tokenType
    }

    override suspend fun getAccessToken(): String? {
        return accessToken
    }

    override suspend fun getTokenType(): String? {
        return tokenType
    }

    override suspend fun getRefreshToken(): String? {
        return refreshToken
    }

    override suspend fun clearTokens() {
        accessToken = null
        refreshToken = null
        tokenType = null
    }
} 