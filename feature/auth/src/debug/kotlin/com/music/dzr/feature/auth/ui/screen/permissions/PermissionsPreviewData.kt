package com.music.dzr.feature.auth.ui.screen.permissions

import com.music.dzr.core.auth.domain.model.PermissionScope

internal val previewPermissions: List<PermissionUiState> =
    PermissionScope.entries.mapIndexed { index, permission ->
        permission.toUiState(isGranted = index % 2 == 0)
    }
