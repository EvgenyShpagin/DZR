package com.music.dzr.core.auth.data.remote.source

import com.music.dzr.core.auth.data.remote.api.AuthApi
import com.music.dzr.core.auth.data.remote.dto.AuthToken
import com.music.dzr.core.network.dto.NetworkResponse

internal class AuthTokenRemoteDataSourceImpl(
    private val authApi: AuthApi
) : AuthTokenRemoteDataSource {

    override suspend fun getToken(
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

    override suspend fun refreshToken(
        refreshToken: String,
        clientId: String
    ): NetworkResponse<AuthToken> {
        return authApi.refreshToken(
            refreshToken = refreshToken,
            clientId = clientId
        )
    }
}
