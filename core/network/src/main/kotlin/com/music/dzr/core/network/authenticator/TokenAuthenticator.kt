package com.music.dzr.core.network.authenticator

import com.music.dzr.core.network.api.AuthApi
import com.music.dzr.core.network.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * An Authenticator that handles automatic refreshing of expired access tokens.
 *
 * This component is responsible for catching 401 Unauthorized errors and attempting to refresh
 * the authentication token. It uses a synchronized block to prevent multiple concurrent refresh
 * attempts.
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

    override fun authenticate(route: Route?, response: Response): Request? {
        // We synchronize to ensure that the token is refreshed only once, even if multiple
        // requests fail with a 401 at the same time.
        synchronized(this) {
            val currentToken = tokenRepository.getAccessToken()
            val failedRequestToken = response.request.header("Authorization")
                ?.substringAfter("Bearer ")

            // If the token in the failed request is different from the current token,
            // it means another thread has already refreshed it. We can retry the request
            // with the new current token.
            if (failedRequestToken != null && failedRequestToken != currentToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            // The token needs to be refreshed.
            val refreshToken = tokenRepository.getRefreshToken()
                ?: return null // No refresh token, we can't refresh.

            // It's necessary to use runBlocking here because OkHttp's Authenticator is synchronous.
            val tokenResponse = runBlocking {
                authApi.refreshToken(
                    refreshToken = refreshToken,
                    clientId = clientId
                )
            }

            return if (tokenResponse.data != null) {
                val newToken = tokenResponse.data
                tokenRepository.onTokenUpdated(newToken)
                // Retry the request with the new token.
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newToken.accessToken}")
                    .build()
            } else {
                // The refresh attempt failed. Clear the tokens and give up.
                tokenResponse.error?.let { tokenRepository.onUpdateFailed(it) }
                null // Returning null tells OkHttp to stop and propagate the 401 error.
            }
        }
    }
} 