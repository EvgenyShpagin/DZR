package com.music.dzr.core.auth.data.remote.source

import com.music.dzr.core.auth.data.remote.api.AuthApi
import com.music.dzr.core.auth.data.remote.dto.AuthToken
import com.music.dzr.core.network.dto.NetworkResponse

/**
 * Remote data source for authentication-related operations.
 * Thin wrapper around [AuthApi] for handling token requests.
 */
internal class AuthTokenRemoteDataSource(private val authApi: AuthApi) {

    /**
     * Exchanges an authorization code for an access token.
     */
    suspend fun getToken(
        code: String,
        redirectUri: String,
        clientId: String,
        codeVerifier: String
    ): NetworkResponse<AuthToken> {
        return authApi.getToken(
            code = code,
            redirectUri = redirectUri,
            clientId = clientId,
            codeVerifier = codeVerifier
        )
    }

    /**
     * Refreshes an expired access token using a refresh token.
     */
    suspend fun refreshToken(
        refreshToken: String,
        clientId: String
    ): NetworkResponse<AuthToken> {
        return authApi.refreshToken(
            refreshToken = refreshToken,
            clientId = clientId
        )
    }
}