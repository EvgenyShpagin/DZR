package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class PermissionsParameterProvider : PreviewParameterProvider<List<PermissionUiState>> {
    override val values = sequenceOf(
        listOf(previewPermissions.first().copy(isGranted = true)),
        listOf(previewPermissions.first().copy(isGranted = false)),
        previewPermissions
    )
}
