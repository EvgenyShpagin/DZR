package com.music.dzr.core.auth.domain.error

import com.music.dzr.core.error.AppError

/**
 * Represents domain-level errors related to authentication and authorization flows.
 *
 * These errors are transport-agnostic and are intended to be consumed by the business logic or UI
 * layers to determine subsequent actions, such as initiating a new sign-in flow, requesting
 * additional permissions, or displaying a user-facing message.
 */
sealed interface AuthError : AppError {
    /**
     * Indicates that no valid authentication token is available locally.
     * This typically requires initiating a new user sign-in flow.
     */
    data object NotAuthenticated : AuthError

    /**
     * The provided authorization grant (e.g., authorization code, refresh token) is invalid,
     * expired, or has been revoked. The user must re-authenticate.
     * Corresponds to the `invalid_grant` error in RFC 6749.
     */
    data object InvalidGrant : AuthError

    /**
     * The user explicitly denied the consent request during the authorization flow.
     * Corresponds to the `access_denied` error in RFC 6749.
     */
    data object AccessDenied : AuthError

    /**
     * The state parameter from the authorization redirect does not match the stored state,
     * indicating a potential Cross-Site Request Forgery (CSRF) attack.
     * The authorization flow must be restarted.
     */
    data object StateMismatch : AuthError

    /**
     * The temporary session data (e.g., PKCE code verifier, CSRF state) is missing or
     * has expired, making it impossible to complete the authorization flow securely.
     * The flow must be restarted.
     */
    data object SessionExpired : AuthError

    /**
     * The requested action could not be performed due to insufficient permissions.
     * This may require requesting additional scopes from the user.
     * Corresponds to the `invalid_scope` error in RFC 6749 or HTTP 403 Forbidden responses.
     */
    data object ScopeInsufficient : AuthError

    /**
     * The client is not registered, not authorized, or is using invalid credentials.
     * This indicates a client-side configuration issue.
     * Corresponds to `invalid_client` or `unauthorized_client` errors in RFC 6749.
     */
    data object InvalidClient : AuthError

    /**
     * The request sent to the authorization or token endpoint was malformed or incomplete.
     * This typically points to a bug in the client implementation.
     * Corresponds to the `invalid_request` error in RFC 6749.
     */
    data object InvalidRequest : AuthError

    /**
     * An unexpected or unclassified error occurred. This serves as a catch-all for issues
     * that do not fit into the other specific error categories.
     */
    data object Unexpected : AuthError

    companion object
}
