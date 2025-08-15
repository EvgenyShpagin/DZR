package com.music.dzr.core.auth.data.remote.source

import com.music.dzr.core.auth.data.remote.api.AuthApi
import com.music.dzr.core.auth.data.remote.dto.AuthToken
import com.music.dzr.core.network.dto.NetworkResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame

internal class AuthRemoteDataSourceTest {

    private lateinit var api: AuthApi
    private lateinit var dataSource: AuthRemoteDataSource

    @BeforeTest
    fun setUp() {
        api = mockk(relaxed = true)
        dataSource = AuthRemoteDataSource(api)
    }

    @Test
    fun getToken_delegatesCall() = runTest {
        val code = "test_code"
        val redirectUri = "test_uri"
        val clientId = "test_client"
        val codeVerifier = "test_verifier"
        val expected = NetworkResponse(data = mockk<AuthToken>())
        coEvery {
            api.getToken(
                code = code,
                redirectUri = redirectUri,
                clientId = clientId,
                codeVerifier = codeVerifier
            )
        } returns expected

        val actual = dataSource.getToken(code, redirectUri, clientId, codeVerifier)

        assertSame(expected, actual)
        coVerify(exactly = 1) {
            api.getToken(
                code = code,
                redirectUri = redirectUri,
                clientId = clientId,
                codeVerifier = codeVerifier
            )
        }
    }

    @Test
    fun refreshToken_delegatesCall() = runTest {
        val refreshToken = "test_refresh_token"
        val clientId = "test_client"
        val expected = NetworkResponse(data = mockk<AuthToken>())
        coEvery {
            api.refreshToken(
                refreshToken = refreshToken,
                clientId = clientId
            )
        } returns expected

        val actual = dataSource.refreshToken(refreshToken, clientId = clientId)

        assertSame(expected, actual)
        coVerify(exactly = 1) {
            api.refreshToken(
                refreshToken = refreshToken,
                clientId = clientId
            )
        }
    }
}