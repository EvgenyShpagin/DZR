package com.music.dzr.core.network.http

import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response


/**
 * An Interceptor that adds the Authorization header to Spotify API requests.
 *
 * This interceptor is responsible for adding the "Authorization: Bearer <token>" header
 * to all outgoing requests.
 * The actual handling of 401 Unauthorized responses is done by [TokenAuthenticator].
 *
 * @param tokenRepository A repository for getting the access token.
 */
internal class AuthInterceptor(
    private val tokenRepository: AuthTokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = runBlocking { tokenRepository.getAccessToken() }
            ?: return chain.proceed(originalRequest)

        val requestWithHeader = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(requestWithHeader)
    }
} 