package com.music.dzr.core.auth.data.remote.oauth

/**
 * Represents a PKCE Code Challenge method, providing both the parameter name for the API
 * and the logic to compute the challenge from a verifier.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7636#section-4.2">RFC 7636 - Section 4.2</a>
 */
internal sealed interface CodeChallengeMethod {

    /**
     * The S256 code challenge method, which uses a SHA-256 hash.
     * This is the mandatory-to-implement method for servers supporting PKCE.
     */
    data object S256 : CodeChallengeMethod {
        /** The string value to be sent in the `code_challenge_method` query parameter. */
        override fun toString() = "S256"
    }

    /**
     * The "plain" code challenge method, which performs no transformation.
     * This is discouraged and should only be used if the client cannot perform SHA-256.
     */
    data object Plain : CodeChallengeMethod {
        /** The string value to be sent in the `code_challenge_method` query parameter. */
        override fun toString() = "plain"
    }
}
