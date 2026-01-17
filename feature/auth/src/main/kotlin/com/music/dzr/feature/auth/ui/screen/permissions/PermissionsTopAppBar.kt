@file:OptIn(ExperimentalMaterial3Api::class)

package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.music.dzr.core.designsystem.component.DzrTopAppBar
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.feature.auth.R

@Composable
fun PermissionsTopAppBar(
    onNavigationClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    DzrTopAppBar(
        modifier = modifier,
        titleRes = R.string.feature_auth_permissions_title,
        onNavigationClick = onNavigationClick,
        scrollBehavior = scrollBehavior
    )
}

@PreviewLightDark
@Composable
private fun PermissionsTopAppBarPreview() {
    DzrTheme {
        Surface {
            PermissionsTopAppBar(
                onNavigationClick = {},
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        }
    }
}
