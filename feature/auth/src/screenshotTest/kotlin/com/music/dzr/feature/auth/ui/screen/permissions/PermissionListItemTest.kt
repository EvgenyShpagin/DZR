package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.component.PreviewLightDarkAndFontScale
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.feature.auth.R

@PreviewLightDarkAndFontScale
@PreviewTest
@Composable
private fun PermissionListItem_Granted_Preview() {
    DzrTheme {
        Surface {
            PermissionListItem(
                state = PermissionListItem(
                    imageVector = DzrIcons.AlternateEmail,
                    title = stringResource(R.string.feature_auth_permissions_email_address),
                    isGranted = true
                ),
                onPermissionToggle = {}
            )
        }
    }
}

@PreviewLightDark
@PreviewTest
@Composable
private fun PermissionListItem_NotGranted_Preview() {
    DzrTheme {
        Surface {
            PermissionListItem(
                state = PermissionListItem(
                    imageVector = DzrIcons.AlternateEmail,
                    title = stringResource(R.string.feature_auth_permissions_email_address),
                    isGranted = false
                ),
                onPermissionToggle = {}
            )
        }
    }
}
