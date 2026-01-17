package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.music.dzr.core.designsystem.component.GroupedList
import com.music.dzr.core.designsystem.theme.DzrTheme

@Composable
fun PermissionList(
    items: List<PermissionUiState>,
    onPermissionToggle: (PermissionUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    GroupedList(modifier = modifier) {
        items.forEach { itemState ->
            PermissionListItem(
                state = itemState,
                onPermissionToggle = {
                    onPermissionToggle(itemState)
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PermissionListPreview() {
    DzrTheme {
        Surface {
            PermissionList(
                items = previewPermissions.take(5),
                onPermissionToggle = {}
            )
        }
    }
}
