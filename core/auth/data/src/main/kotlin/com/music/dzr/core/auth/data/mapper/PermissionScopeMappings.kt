package com.music.dzr.core.auth.data.mapper

import com.music.dzr.core.auth.data.model.AuthScope
import com.music.dzr.core.auth.domain.model.PermissionScope

/**
 * Maps domain [PermissionScope] (business intent) to [AuthScope] (API contract).
 * This mapping defines the specific OAuth strings required by the Spotify API.
 */
internal fun PermissionScope.toAuthScope(): AuthScope = when (this) {
    PermissionScope.UgcImageUpload -> AuthScope("ugc-image-upload")
    PermissionScope.UserReadPlaybackState -> AuthScope("user-read-playback-state")
    PermissionScope.UserModifyPlaybackState -> AuthScope("user-modify-playback-state")
    PermissionScope.UserReadCurrentlyPlaying -> AuthScope("user-read-currently-playing")
    PermissionScope.AppRemoteControl -> AuthScope("app-remote-control")
    PermissionScope.Streaming -> AuthScope("streaming")
    PermissionScope.PlaylistReadPrivate -> AuthScope("playlist-read-private")
    PermissionScope.PlaylistReadCollaborative -> AuthScope("playlist-read-collaborative")
    PermissionScope.PlaylistModifyPrivate -> AuthScope("playlist-modify-private")
    PermissionScope.PlaylistModifyPublic -> AuthScope("playlist-modify-public")
    PermissionScope.UserFollowModify -> AuthScope("user-follow-modify")
    PermissionScope.UserFollowRead -> AuthScope("user-follow-read")
    PermissionScope.UserTopRead -> AuthScope("user-top-read")
    PermissionScope.UserReadRecentlyPlayed -> AuthScope("user-read-recently-played")
    PermissionScope.UserLibraryModify -> AuthScope("user-library-modify")
    PermissionScope.UserLibraryRead -> AuthScope("user-library-read")
    PermissionScope.UserReadEmail -> AuthScope("user-read-email")
    PermissionScope.UserReadPrivate -> AuthScope("user-read-private")
}

/**
 * Parses an OAuth scope string into the corresponding domain [PermissionScope].
 *
 * Use this when converting API scope strings (e.g. received from storage or a callback response)
 * back into domain permissions. Returns `null` if the scope is not recognized.
 */
internal fun PermissionScope.Companion.fromString(value: String): PermissionScope {
    return when (value) {
        "ugc-image-upload" -> PermissionScope.UgcImageUpload
        "user-read-playback-state" -> PermissionScope.UserReadPlaybackState
        "user-modify-playback-state" -> PermissionScope.UserModifyPlaybackState
        "user-read-currently-playing" -> PermissionScope.UserReadCurrentlyPlaying
        "app-remote-control" -> PermissionScope.AppRemoteControl
        "streaming" -> PermissionScope.Streaming
        "playlist-read-private" -> PermissionScope.PlaylistReadPrivate
        "playlist-read-collaborative" -> PermissionScope.PlaylistReadCollaborative
        "playlist-modify-private" -> PermissionScope.PlaylistModifyPrivate
        "playlist-modify-public" -> PermissionScope.PlaylistModifyPublic
        "user-follow-modify" -> PermissionScope.UserFollowModify
        "user-follow-read" -> PermissionScope.UserFollowRead
        "user-top-read" -> PermissionScope.UserTopRead
        "user-read-recently-played" -> PermissionScope.UserReadRecentlyPlayed
        "user-library-modify" -> PermissionScope.UserLibraryModify
        "user-library-read" -> PermissionScope.UserLibraryRead
        "user-read-email" -> PermissionScope.UserReadEmail
        "user-read-private" -> PermissionScope.UserReadPrivate
        else -> throw IllegalArgumentException("Invalid AuthScope: $value")
    }
}
