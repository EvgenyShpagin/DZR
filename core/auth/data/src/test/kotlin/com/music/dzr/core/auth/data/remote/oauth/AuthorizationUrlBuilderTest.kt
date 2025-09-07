package com.music.dzr.core.auth.data.remote.oauth

import com.music.dzr.core.network.BuildConfig
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthorizationUrlBuilderTest {

    private val clientId = "test_client_id"
    private val redirectUri = "com.music.dzr.app://callback"
    private val scope = "user-read-private user-read-email"
    private val state = "a_random_state_string_for_security"
    private val codeChallenge = "a_pkce_generated_code_challenge"

    private val urlBuilder = AuthorizationUrlBuilder(clientId)

    @Test
    fun build_constructsCorrectUrl() {
        // Arrange
        val baseUrl = URL(BuildConfig.SPOTIFY_ACCOUNTS_URL)

        // Act
        val urlString = urlBuilder.build(
            redirectUri = redirectUri,
            scope = scope,
            state = state,
            codeChallenge = codeChallenge
        )

        // Assert
        val httpUrl = urlString.toHttpUrl()

        // Check the base components of the URL
        assertEquals("https", httpUrl.scheme)
        assertEquals(baseUrl.host, httpUrl.host)
        assertEquals("/authorize", httpUrl.encodedPath)

        // Check each query parameter to ensure it's correctly set
        assertEquals("code", httpUrl.queryParameter("response_type"))
        assertEquals(clientId, httpUrl.queryParameter("client_id"))
        assertEquals(scope, httpUrl.queryParameter("scope"))
        assertEquals(redirectUri, httpUrl.queryParameter("redirect_uri"))
        assertEquals(state, httpUrl.queryParameter("state"))
        assertEquals("S256", httpUrl.queryParameter("code_challenge_method"))
        assertEquals(codeChallenge, httpUrl.queryParameter("code_challenge"))
    }
}