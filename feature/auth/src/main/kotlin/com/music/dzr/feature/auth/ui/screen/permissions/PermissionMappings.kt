package com.music.dzr.feature.auth.ui.screen.permissions

import com.music.dzr.core.auth.domain.model.PermissionScope
import com.music.dzr.core.auth.domain.model.PermissionScope.AppRemoteControl
import com.music.dzr.core.auth.domain.model.PermissionScope.PlaylistModifyPrivate
import com.music.dzr.core.auth.domain.model.PermissionScope.PlaylistModifyPublic
import com.music.dzr.core.auth.domain.model.PermissionScope.PlaylistReadCollaborative
import com.music.dzr.core.auth.domain.model.PermissionScope.PlaylistReadPrivate
import com.music.dzr.core.auth.domain.model.PermissionScope.Streaming
import com.music.dzr.core.auth.domain.model.PermissionScope.UgcImageUpload
import com.music.dzr.core.auth.domain.model.PermissionScope.UserFollowModify
import com.music.dzr.core.auth.domain.model.PermissionScope.UserFollowRead
import com.music.dzr.core.auth.domain.model.PermissionScope.UserLibraryModify
import com.music.dzr.core.auth.domain.model.PermissionScope.UserLibraryRead
import com.music.dzr.core.auth.domain.model.PermissionScope.UserModifyPlaybackState
import com.music.dzr.core.auth.domain.model.PermissionScope.UserReadCurrentlyPlaying
import com.music.dzr.core.auth.domain.model.PermissionScope.UserReadEmail
import com.music.dzr.core.auth.domain.model.PermissionScope.UserReadPlaybackState
import com.music.dzr.core.auth.domain.model.PermissionScope.UserReadPrivate
import com.music.dzr.core.auth.domain.model.PermissionScope.UserReadRecentlyPlayed
import com.music.dzr.core.auth.domain.model.PermissionScope.UserTopRead
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.feature.auth.R

internal fun PermissionUiState.toDomain() = when (titleRes) {
    R.string.feature_auth_permissions_account_type_and_premium_status -> UserReadPrivate
    R.string.feature_auth_permissions_email_address -> UserReadEmail
    R.string.feature_auth_permissions_music_streaming -> Streaming
    R.string.feature_auth_permissions_read_playback_activity -> UserReadPlaybackState
    R.string.feature_auth_permissions_see_whats_playing -> UserReadCurrentlyPlaying
    R.string.feature_auth_permissions_control_playback -> UserModifyPlaybackState
    R.string.feature_auth_permissions_remote_app_control -> AppRemoteControl
    R.string.feature_auth_permissions_read_library -> UserLibraryRead
    R.string.feature_auth_permissions_manage_library -> UserLibraryModify
    R.string.feature_auth_permissions_read_private_playlists -> PlaylistReadPrivate
    R.string.feature_auth_permissions_read_collaborative_playlists -> PlaylistReadCollaborative
    R.string.feature_auth_permissions_manage_public_playlists -> PlaylistModifyPublic
    R.string.feature_auth_permissions_manage_private_playlists -> PlaylistModifyPrivate
    R.string.feature_auth_permissions_read_top_music -> UserTopRead
    R.string.feature_auth_permissions_read_recently_played -> UserReadRecentlyPlayed
    R.string.feature_auth_permissions_read_follows -> UserFollowRead
    R.string.feature_auth_permissions_manage_follows -> UserFollowModify
    R.string.feature_auth_permissions_upload_images -> UgcImageUpload
    else -> throw IllegalArgumentException(
        "Unknown PermissionUiState.titleRes=$titleRes. Cannot map to PermissionScope."
    )
}

internal fun PermissionScope.toUiState(isGranted: Boolean = true) = when (this) {
    UgcImageUpload -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_upload_images,
        imageVector = DzrIcons.AddPhotoAlternate,
        isGranted = isGranted
    )

    UserReadPlaybackState -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_read_playback_activity,
        imageVector = DzrIcons.History,
        isGranted = isGranted
    )

    UserModifyPlaybackState -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_control_playback,
        imageVector = DzrIcons.SkipNext,
        isGranted = isGranted
    )

    UserReadCurrentlyPlaying -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_see_whats_playing,
        imageVector = DzrIcons.QueueMusic,
        isGranted = isGranted
    )

    AppRemoteControl -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_remote_app_control,
        imageVector = DzrIcons.SettingsRemote,
        isGranted = isGranted
    )

    Streaming -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_music_streaming,
        imageVector = DzrIcons.PlayCircle,
        isGranted = isGranted
    )

    PlaylistReadPrivate -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_read_private_playlists,
        imageVector = DzrIcons.VisibilityOff,
        isGranted = isGranted
    )

    PlaylistReadCollaborative -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_read_collaborative_playlists,
        imageVector = DzrIcons.SupervisorAccount,
        isGranted = isGranted
    )

    PlaylistModifyPrivate -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_manage_private_playlists,
        imageVector = DzrIcons.EnhancedEncryption,
        isGranted = isGranted
    )

    PlaylistModifyPublic -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_manage_public_playlists,
        imageVector = DzrIcons.LockOpenRight,
        isGranted = isGranted
    )

    UserFollowModify -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_manage_follows,
        imageVector = DzrIcons.ManageAccounts,
        isGranted = isGranted
    )

    UserFollowRead -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_read_follows,
        imageVector = DzrIcons.PersonSearch,
        isGranted = isGranted
    )

    UserTopRead -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_read_top_music,
        imageVector = DzrIcons.StarRate,
        isGranted = isGranted
    )

    UserReadRecentlyPlayed -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_read_recently_played,
        imageVector = DzrIcons.MusicHistory,
        isGranted = isGranted
    )

    UserLibraryModify -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_manage_library,
        imageVector = DzrIcons.Edit,
        isGranted = isGranted
    )

    UserLibraryRead -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_read_library,
        imageVector = DzrIcons.LibraryMusic,
        isGranted = isGranted
    )

    UserReadEmail -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_email_address,
        imageVector = DzrIcons.AlternateEmail,
        isGranted = isGranted
    )

    UserReadPrivate -> PermissionUiState(
        titleRes = R.string.feature_auth_permissions_account_type_and_premium_status,
        imageVector = DzrIcons.AccountCircle,
        isGranted = isGranted
    )
}
