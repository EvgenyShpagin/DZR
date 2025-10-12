package com.music.dzr.core.auth.data.remote.http

import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
import com.music.dzr.core.auth.domain.repository.getAccessToken
import com.music.dzr.core.result.isFailure
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An Interceptor that adds the Authorization header to Spotify API requests.
 *
 * This interceptor is responsible for adding the "Authorization: Bearer <token>" header
 * to all outgoing requests if the request doesn't already have an Authorization header.
 * The actual handling of 401 Unauthorized responses is done by [AuthTokenAuthenticator].
 *
 * @param tokenRepository A repository for getting the access token.
 */
internal class AuthInterceptor(
    private val tokenRepository: AuthTokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.header("Authorization") != null) {
            return chain.proceed(originalRequest)
        }

        val accessToken = runBlocking { tokenRepository.getAccessToken() }
        if (accessToken.isFailure()) {
            return chain.proceed(originalRequest)
        }

        val requestWithHeader = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${accessToken.data}")
            .build()

        return chain.proceed(requestWithHeader)
    }
}
