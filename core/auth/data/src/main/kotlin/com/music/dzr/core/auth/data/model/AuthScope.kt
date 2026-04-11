package com.music.dzr.core.auth.data.model

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
    companion object {
        fun Collection<AuthScope>.join(): String =
            joinToString(" ") { scope -> scope.value }

        fun parse(scopes: String): List<AuthScope> =
            scopes.split(' ')
                .filter { it.isNotBlank() }
                .map { scopeValue -> AuthScope(scopeValue) }

        val UgcImageUpload = AuthScope("ugc-image-upload")
        val UserReadPlaybackState = AuthScope("user-read-playback-state")
        val UserModifyPlaybackState = AuthScope("user-modify-playback-state")
        val UserReadCurrentlyPlaying = AuthScope("user-read-currently-playing")
        val AppRemoteControl = AuthScope("app-remote-control")
        val Streaming = AuthScope("streaming")
        val PlaylistReadPrivate = AuthScope("playlist-read-private")
        val PlaylistReadCollaborative = AuthScope("playlist-read-collaborative")
        val PlaylistModifyPrivate = AuthScope("playlist-modify-private")
        val PlaylistModifyPublic = AuthScope("playlist-modify-public")
        val UserFollowModify = AuthScope("user-follow-modify")
        val UserFollowRead = AuthScope("user-follow-read")
        val UserTopRead = AuthScope("user-top-read")
        val UserReadRecentlyPlayed = AuthScope("user-read-recently-played")
        val UserLibraryModify = AuthScope("user-library-modify")
        val UserLibraryRead = AuthScope("user-library-read")
        val UserReadEmail = AuthScope("user-read-email")
        val UserReadPrivate = AuthScope("user-read-private")
    }
}
