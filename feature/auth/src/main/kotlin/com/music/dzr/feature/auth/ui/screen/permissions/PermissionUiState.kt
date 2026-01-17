package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class PermissionUiState(
    @param:StringRes val titleRes: Int,
    val imageVector: ImageVector,
    val isGranted: Boolean = false
)
