package com.music.dzr.core.auth.domain.model

/**
 * Represents an OAuth 2.0 scope as defined in RFC 6749, Section 3.3.
 *
 * OAuth scopes provide a way to limit the access granted to an access token.
 * The scope parameter is a space-delimited list of case-sensitive strings
 * that represent the requested scope of access.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-3.3">RFC 6749, Section 3.3</a>
 */
@JvmInline
value class AuthScope(
    /**
     * The string value of the scope as it appears in OAuth requests and responses.
     *
     * The value is case-sensitive and follows the scope-token syntax defined in RFC 6749.
     * Scope tokens are typically separated by spaces when multiple scopes are present.
     */
    val value: String
) {
    init {
        require(value.isNotBlank()) { "Scope value cannot be blank" }
        require(' ' !in value) { "Scope value cannot contain spaces" }
        require(value.all { it.isValidScopeChar() }) {
            "Scope value contains invalid characters. " +
                    "Only ASCII letters, digits, and specific symbols are allowed"
        }

    }

    companion object {

        private fun Char.isValidScopeChar(): Boolean {
            // RFC 6749: scope-token characters are %x21, %x23-5B, %x5D-7E
            return this in '\u0021'..'\u007E' && this != '"' && this != '\\'
        }


        fun Collection<AuthScope>.join(): String =
            joinToString(" ") { scope -> scope.value }

        fun parse(scopes: String): List<AuthScope> =
            scopes.split(' ')
                .filter { it.isNotBlank() }
                .map { scopeValue -> AuthScope(scopeValue) }
    }
}
