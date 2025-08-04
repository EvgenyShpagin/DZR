package com.music.dzr.core.network.http

import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
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
 */
internal class TokenAuthenticator(
    private val tokenRepository: AuthTokenRepository
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
            val isSuccessful = tokenRepository.refreshToken()

            if (!isSuccessful) {
                // For any refresh error, fail the original request.
                return@runBlocking null
            }

            val newToken = tokenRepository.getAccessToken()
            // Retry the request with the new token.
            return@runBlocking response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        }
    }
} 