package com.music.dzr.feature.auth.ui.screen.permissions

import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.feature.auth.R

internal val previewPermissions: List<PermissionUiState> = listOf(
    PermissionUiState(
        titleRes = R.string.feature_auth_permissions_account_type_and_premium_status,
        imageVector = DzrIcons.AccountCircle,
        isGranted = true
    ),
    PermissionUiState(
        titleRes = R.string.feature_auth_permissions_email_address,
        imageVector = DzrIcons.AlternateEmail,
        isGranted = false
    ),
    PermissionUiState(
        titleRes = R.string.feature_auth_permissions_music_streaming,
        imageVector = DzrIcons.PlayCircle,
        isGranted = true
    ),
    PermissionUiState(
        titleRes = R.string.feature_auth_permissions_read_playback_activity,
        imageVector = DzrIcons.History,
        isGranted = false
    ),
    PermissionUiState(
        titleRes = R.string.feature_auth_permissions_see_whats_playing,
        imageVector = DzrIcons.QueueMusic,
        isGranted = true
    )
)
