package com.music.dzr.core.network.authenticator

import com.music.dzr.core.network.api.AuthApi
import com.music.dzr.core.network.model.auth.toDomain
import com.music.dzr.core.network.model.error.NetworkErrorType
import com.music.dzr.core.oauth.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * An Authenticator that handles automatic refreshing of expired access tokens.
 * It catches 401 auth errors and attempts to refresh the token.
 *
 * @param tokenRepository A repository to get and update tokens.
 * @param clientId The application's client ID.
 * @param authApi The API service for refreshing tokens.
 */
internal class TokenAuthenticator(
    private val tokenRepository: TokenRepository,
    private val clientId: String,
    private val authApi: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? = synchronized(this) {
        // We synchronize to ensure that the token is refreshed only once.

        // As the Authenticator is a synchronous component,
        // a blocking call is necessary to execute the suspend functions.
        return runBlocking {
            val currentToken = tokenRepository.getAccessToken()
            val failedRequestToken = response.request.header("Authorization")
                ?.substringAfter("Bearer ")

            // If the token has already been refreshed by another thread, retry with the new token.
            if (failedRequestToken != null && failedRequestToken != currentToken) {
                return@runBlocking response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            val refreshToken = tokenRepository.getRefreshToken()
                ?: return@runBlocking null // No refresh token, we can't refresh.

            val tokenResponse = authApi.refreshToken(
                refreshToken = refreshToken,
                clientId = clientId
            )

            val newToken = tokenResponse.data
            if (tokenResponse.error == null && newToken != null) {
                tokenRepository.saveToken(newToken.toDomain())
                // Retry the request with the new token.
                return@runBlocking response.request.newBuilder()
                    .header("Authorization", "Bearer ${newToken.accessToken}")
                    .build()
            } else {
                val error = tokenResponse.error
                // If the refresh token is invalid, clear tokens to force re-login.
                val isUnrecoverableAuthError =
                    error?.type == NetworkErrorType.HttpException && error.reason == "invalid_grant"

                if (isUnrecoverableAuthError) {
                    tokenRepository.clearTokens()
                }
                // For any refresh error, fail the original request.
                return@runBlocking null
            }
        }
    }
}