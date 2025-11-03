package com.music.dzr.core.auth.data.remote.source

import com.music.dzr.core.auth.data.remote.dto.AuthToken
import com.music.dzr.core.data.test.HasForcedError
import com.music.dzr.core.data.test.runUnlessForcedError
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.error.NetworkError
import kotlin.random.Random

/**
 * Configurable in-memory test implementation of [AuthTokenRemoteDataSource] with default data.
 *
 * State is set via constructor or direct property assignment. Set [forcedError] to return failures.
 *
 * Not thread-safe.
 */
internal class TestAuthTokenRemoteDataSource(
    var exchangedToken: AuthToken = DefaultToken,
    var refreshedToken: AuthToken = DefaultRefreshedToken
) : AuthTokenRemoteDataSource, HasForcedError<NetworkError> {

    override var forcedError: NetworkError? = null

    override suspend fun getToken(
        code: String,
        redirectUri: String,
        clientId: String,
        codeVerifier: String
    ): NetworkResponse<AuthToken> = runUnlessForcedError {
        exchangedToken
    }

    override suspend fun refreshToken(
        refreshToken: String,
        clientId: String
    ): NetworkResponse<AuthToken> = runUnlessForcedError {
        refreshedToken
    }

    private companion object {
        val DefaultToken = AuthToken(
            accessToken = "access-token",
            refreshToken = "refresh-token",
            expiresIn = 3600,
            scope = null,
            tokenType = "Bearer"
        )

        val DefaultRefreshedToken = AuthToken(
            accessToken = "refreshed-access-token-${Random.nextInt(999)}",
            refreshToken = "refreshed-refresh-token-${Random.nextInt(999)}",
            expiresIn = 3600,
            scope = null,
            tokenType = "Bearer"
        )
    }
}
