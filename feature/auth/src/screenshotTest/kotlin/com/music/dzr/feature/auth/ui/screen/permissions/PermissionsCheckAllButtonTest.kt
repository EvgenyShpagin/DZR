package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewLightDark
@PreviewTest
@Composable
private fun PermissionsCheckAllButton_Checked_Preview() {
    DzrTheme {
        PermissionsCheckAllButton(
            onClick = {},
            areAllChecked = true
        )
    }
}

@PreviewLightDark
@PreviewTest
@Composable
private fun PermissionsCheckAllButton_Unchecked_Preview() {
    DzrTheme {
        PermissionsCheckAllButton(
            onClick = {},
            areAllChecked = false
        )
    }
}
