package com.music.dzr.core.auth.data.mapper

import com.music.dzr.core.auth.data.model.AuthScope
import com.music.dzr.core.auth.domain.model.PermissionScope

/**
 * Maps domain [PermissionScope] (business intent) to [AuthScope] (API contract).
 * This mapping defines the specific OAuth strings required by the Spotify API.
 */
internal fun PermissionScope.toAuthScope(): AuthScope = when (this) {
    PermissionScope.UgcImageUpload -> AuthScope.UgcImageUpload
    PermissionScope.UserReadPlaybackState -> AuthScope.UserReadPlaybackState
    PermissionScope.UserModifyPlaybackState -> AuthScope.UserModifyPlaybackState
    PermissionScope.UserReadCurrentlyPlaying -> AuthScope.UserReadCurrentlyPlaying
    PermissionScope.AppRemoteControl -> AuthScope.AppRemoteControl
    PermissionScope.Streaming -> AuthScope.Streaming
    PermissionScope.PlaylistReadPrivate -> AuthScope.PlaylistReadPrivate
    PermissionScope.PlaylistReadCollaborative -> AuthScope.PlaylistReadCollaborative
    PermissionScope.PlaylistModifyPrivate -> AuthScope.PlaylistModifyPrivate
    PermissionScope.PlaylistModifyPublic -> AuthScope.PlaylistModifyPublic
    PermissionScope.UserFollowModify -> AuthScope.UserFollowModify
    PermissionScope.UserFollowRead -> AuthScope.UserFollowRead
    PermissionScope.UserTopRead -> AuthScope.UserTopRead
    PermissionScope.UserReadRecentlyPlayed -> AuthScope.UserReadRecentlyPlayed
    PermissionScope.UserLibraryModify -> AuthScope.UserLibraryModify
    PermissionScope.UserLibraryRead -> AuthScope.UserLibraryRead
    PermissionScope.UserReadEmail -> AuthScope.UserReadEmail
    PermissionScope.UserReadPrivate -> AuthScope.UserReadPrivate
}

/**
 * Maps an [AuthScope] to domain [PermissionScope].
 */
internal fun AuthScope.toDomain(): PermissionScope {
    return when (this) {
        AuthScope.UgcImageUpload -> PermissionScope.UgcImageUpload
        AuthScope.UserReadPlaybackState -> PermissionScope.UserReadPlaybackState
        AuthScope.UserModifyPlaybackState -> PermissionScope.UserModifyPlaybackState
        AuthScope.UserReadCurrentlyPlaying -> PermissionScope.UserReadCurrentlyPlaying
        AuthScope.AppRemoteControl -> PermissionScope.AppRemoteControl
        AuthScope.Streaming -> PermissionScope.Streaming
        AuthScope.PlaylistReadPrivate -> PermissionScope.PlaylistReadPrivate
        AuthScope.PlaylistReadCollaborative -> PermissionScope.PlaylistReadCollaborative
        AuthScope.PlaylistModifyPrivate -> PermissionScope.PlaylistModifyPrivate
        AuthScope.PlaylistModifyPublic -> PermissionScope.PlaylistModifyPublic
        AuthScope.UserFollowModify -> PermissionScope.UserFollowModify
        AuthScope.UserFollowRead -> PermissionScope.UserFollowRead
        AuthScope.UserTopRead -> PermissionScope.UserTopRead
        AuthScope.UserReadRecentlyPlayed -> PermissionScope.UserReadRecentlyPlayed
        AuthScope.UserLibraryModify -> PermissionScope.UserLibraryModify
        AuthScope.UserLibraryRead -> PermissionScope.UserLibraryRead
        AuthScope.UserReadEmail -> PermissionScope.UserReadEmail
        AuthScope.UserReadPrivate -> PermissionScope.UserReadPrivate
        else -> throw IllegalArgumentException("Invalid AuthScope: $value")
    }
}
