package com.music.dzr.core.auth.data.mapper

import com.music.dzr.core.auth.data.remote.model.RedirectUriParams
import com.music.dzr.core.auth.domain.error.AuthError
import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.error.AppError
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.network.dto.error.NetworkErrorType
import com.music.dzr.core.storage.error.StorageError

/**
 * Maps a [NetworkError] from the token exchange flow to a domain-specific [AuthError].
 *
 * This mapping handles errors returned from the OAuth 2.0 token endpoint, such as when
 * exchanging an authorization code or refreshing a token.
 */
internal fun NetworkError.toDomain(): AppError {
    return if (this.type == NetworkErrorType.HttpException) {
        when (this.reason) {
            "invalid_grant" -> AuthError.InvalidGrant
            "invalid_client",
            "unauthorized_client" -> AuthError.InvalidClient

            "unsupported_grant_type",
            "invalid_request" -> AuthError.InvalidRequest

            "invalid_scope" -> AuthError.ScopeInsufficient
            else -> toDomain()
        }
    } else {
        toDomain()
    }
}

/**
 * Maps an error string from an OAuth 2.0 authorization redirect to a domain-specific [AuthError].
 *
 * These errors are defined in
 * [RFC 6749, Section 4.1.2.1](https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1),
 * and are passed as a `error` query parameter in the redirect URI.
 */
internal fun RedirectUriParams.Error.toDomain(): AuthError {
    return when (error) {
        "invalid_request",
        "unsupported_response_type" -> AuthError.InvalidRequest

        "unauthorized_client" -> AuthError.InvalidClient
        "access_denied" -> AuthError.AccessDenied
        "invalid_scope" -> AuthError.ScopeInsufficient

        "server_error",
        "temporarily_unavailable" -> AuthError.Unexpected

        else -> AuthError.Unexpected
    }
}

/**
 * Maps a [StorageError] from local data sources to a domain-specific [AuthError] or
 * [com.music.dzr.core.error.PersistenceError].
 */
internal fun StorageError.toDomain(): AppError = when (this) {
    StorageError.NotFound -> AuthError.NotAuthenticated
    else -> toDomain() // => PersistenceError
}
