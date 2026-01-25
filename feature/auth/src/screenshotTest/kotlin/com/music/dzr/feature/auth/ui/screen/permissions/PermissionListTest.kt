package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.component.PreviewLightDarkAndFontScale
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewLightDarkAndFontScale
@PreviewTest
@Composable
private fun PermissionList_Preview() {
    DzrTheme {
        PermissionList(
            items = previewPermissions,
            onPermissionToggle = {}
        )
    }
}
