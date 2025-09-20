package com.music.dzr.core.auth.data.local.model

/**
 * Represents a short-lived snapshot of parameters required to complete the
 * OAuth 2.0 Authorization Code flow with PKCE after the browser/custom tab redirect.
 *
 * This model is persisted so the app can recover from process death between
 * initiating the authorization request and handling the redirect callback.
 */
data class AuthSession(
    /**
     * The PKCE `code_verifier` generated for this authorization attempt (RFC 7636 §4.1).
     * It is later sent to the token endpoint when exchanging the authorization code for tokens.
     */
    val codeVerifier: String,
    /**
     * The OAuth 2.0 `state` value used for CSRF protection (RFC 6749 §10.12).
     * It must match the `state` returned in the redirect.
     */
    val csrfState: String,
    /**
     * Epoch milliseconds when this session snapshot was created.
     * Can be used to enforce an upper lifetime if needed.
     */
    val createdAtMillis: Long
)
