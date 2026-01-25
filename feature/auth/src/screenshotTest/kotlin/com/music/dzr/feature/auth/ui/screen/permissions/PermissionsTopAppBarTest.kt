package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.component.PreviewLightDarkAndFontScale
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDarkAndFontScale
@Composable
private fun PermissionsTopAppBar_Preview() {
    DzrTheme {
        Surface {
            @OptIn(ExperimentalMaterial3Api::class)
            PermissionsTopAppBar(
                onNavigationClick = {},
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        }
    }
}
