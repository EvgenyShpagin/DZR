package com.music.dzr.core.oauth.model

/**
 * Represents a standard error response from an OAuth 2.0 token endpoint,
 * as defined in RFC 6749, Section 5.2.
 *
 * @property code The error code.
 * @property description A human-readable description of the error (optional).
 * @property uri A URI to a page with information about the error (optional).
 *
 * @see <a href="https://tools.ietf.org/html/rfc6749#section-5.2">RFC 6749, Section 5.2</a>
 */
data class OAuthError(
    val code: Code,
    val description: String? = null,
    val uri: String? = null
) {
    /**
     * A wrapper for the OAuth 2.0 error code string.
     *
     * @property value The raw string value of the error code (e.g., "invalid_grant").
     */
    @JvmInline
    value class Code private constructor(val value: String) {
        companion object {
            val InvalidRequest: Code = Code("invalid_request")
            val InvalidClient: Code = Code("invalid_client")
            val InvalidGrant: Code = Code("invalid_grant")
            val UnauthorizedClient: Code = Code("unauthorized_client")
            val UnsupportedGrantType: Code = Code("unsupported_grant_type")
            val InvalidScope: Code = Code("invalid_scope")

            val entries = listOf(
                InvalidRequest,
                InvalidClient,
                InvalidGrant,
                UnauthorizedClient,
                UnsupportedGrantType,
                InvalidScope
            )
        }
    }
}