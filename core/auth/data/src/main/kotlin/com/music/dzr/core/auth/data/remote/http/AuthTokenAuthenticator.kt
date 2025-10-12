package com.music.dzr.core.auth.data.remote.http

import androidx.annotation.VisibleForTesting
import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
import com.music.dzr.core.auth.domain.repository.getAccessToken
import com.music.dzr.core.result.isFailure
import com.music.dzr.core.result.onFailure
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
internal class AuthTokenAuthenticator(
    private val tokenRepository: AuthTokenRepository
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? = synchronized(this) {
        // We synchronize to ensure that the token is refreshed only once.

        // As the Authenticator is a synchronous component,
        // a blocking call is necessary to execute the suspend functions.
        return runBlocking {
            // Prevent potential infinite retry loops on repeated 401s
            if (response.priorResponseCount() >= MAX_RETRIES) return@runBlocking null

            val currentToken = tokenRepository.getAccessToken()
            if (currentToken.isFailure()) return@runBlocking null
            val failedRequestToken = response.request.header("Authorization")
                ?.substringAfter("Bearer ")

            // If the token has already been refreshed by another thread, retry with the new token.
            if (failedRequestToken != null && failedRequestToken != currentToken.data) {
                return@runBlocking response.request.newBuilder()
                    .header("Authorization", "Bearer ${currentToken.data}")
                    .build()
            }
            tokenRepository.refreshToken().onFailure {
                // For any refresh error, fail the original request.
                return@runBlocking null
            }

            val newToken = tokenRepository.getAccessToken()
            if (newToken.isFailure()) return@runBlocking null

            // Retry the request with the new token.
            return@runBlocking response.request.newBuilder()
                .header("Authorization", "Bearer ${newToken.data}")
                .build()
        }
    }

    private fun Response.priorResponseCount(): Int {
        var result = 1
        var prior = priorResponse
        while (prior != null) {
            result++
            prior = prior.priorResponse
        }
        return result
    }

    companion object {
        @VisibleForTesting
        const val MAX_RETRIES = 2
    }
}