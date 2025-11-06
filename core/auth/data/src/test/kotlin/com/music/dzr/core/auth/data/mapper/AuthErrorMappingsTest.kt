package com.music.dzr.core.auth.data.mapper

import com.music.dzr.core.auth.data.remote.model.RedirectUriParams
import com.music.dzr.core.auth.domain.error.AuthError
import com.music.dzr.core.error.ConnectivityError
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.network.dto.error.NetworkErrorType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AuthErrorMappingsTest {

    @Test
    fun networkError_toDomain_mapsOAuthHttpReasons() {
        assertEquals(
            expected = AuthError.InvalidGrant,
            actual = NetworkError(
                NetworkErrorType.HttpException,
                message = "",
                code = 401,
                reason = "invalid_grant"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.InvalidClient,
            actual = NetworkError(
                NetworkErrorType.HttpException,
                message = "",
                code = 400,
                reason = "invalid_client"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.InvalidClient,
            actual = NetworkError(
                NetworkErrorType.HttpException,
                message = "",
                code = 400,
                reason = "unauthorized_client"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.InvalidRequest,
            actual = NetworkError(
                NetworkErrorType.HttpException,
                message = "",
                code = 400,
                reason = "invalid_request"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.InvalidRequest,
            actual = NetworkError(
                NetworkErrorType.HttpException,
                message = "",
                code = 400,
                reason = "unsupported_grant_type"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.ScopeInsufficient,
            actual = NetworkError(
                NetworkErrorType.HttpException,
                message = "",
                code = 400,
                reason = "invalid_scope"
            ).toDomain()
        )
    }

    @Test
    fun networkError_toDomain_delegatesNonHttpToCoreMappings() {
        // Non-HTTP should be delegated to core mapper.
        val delegated = NetworkError(NetworkErrorType.Timeout, message = "", code = 0).toDomain()
        assertIs<ConnectivityError>(delegated)
    }

    @Test
    fun redirectError_toDomain_mapsPerRFC() {
        assertEquals(
            expected = AuthError.InvalidRequest,
            actual = RedirectUriParams.Error(
                error = "invalid_request",
                errorDescription = null,
                errorUri = null,
                state = "S"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.InvalidRequest,
            actual = RedirectUriParams.Error(
                error = "unsupported_response_type",
                errorDescription = null,
                errorUri = null,
                state = "S"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.InvalidClient,
            actual = RedirectUriParams.Error(
                error = "unauthorized_client",
                errorDescription = null,
                errorUri = null,
                state = "S"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.AccessDenied,
            actual = RedirectUriParams.Error(
                error = "access_denied",
                errorDescription = null,
                errorUri = null,
                state = "S"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.ScopeInsufficient,
            actual = RedirectUriParams.Error(
                error = "invalid_scope",
                errorDescription = null,
                errorUri = null,
                state = "S"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.Unexpected,
            actual = RedirectUriParams.Error(
                error = "server_error",
                errorDescription = null,
                errorUri = null,
                state = "S"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.Unexpected,
            actual = RedirectUriParams.Error(
                error = "temporarily_unavailable",
                errorDescription = null,
                errorUri = null,
                state = "S"
            ).toDomain()
        )

        assertEquals(
            expected = AuthError.Unexpected,
            actual = RedirectUriParams.Error(
                error = "something_else",
                errorDescription = null,
                errorUri = null,
                state = "S"
            ).toDomain()
        )
    }
}
