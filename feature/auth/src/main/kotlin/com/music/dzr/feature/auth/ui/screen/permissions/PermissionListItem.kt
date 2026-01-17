package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.component.GroupedListItem
import com.music.dzr.core.designsystem.theme.DzrTheme

@Composable
fun PermissionListItem(
    state: PermissionUiState,
    onPermissionToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    GroupedListItem(
        modifier = modifier.height(48.dp),
        leadingIcon = {
            Icon(
                imageVector = state.imageVector,
                contentDescription = null
            )
        },
        leadingText = {
            Text(stringResource(state.titleRes))
        },
        trailingIcon = {
            Switch(
                checked = state.isGranted,
                onCheckedChange = { onPermissionToggle() }
            )
        },
        onClick = { onPermissionToggle() }
    )
}

@PreviewLightDark
@Composable
private fun PermissionListItemGrantedPreview() {
    DzrTheme {
        Surface {
            PermissionListItem(
                state = previewPermissions.first().copy(isGranted = true),
                onPermissionToggle = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PermissionListItemNotGrantedPreview() {
    DzrTheme {
        Surface {
            PermissionListItem(
                state = previewPermissions.first().copy(isGranted = false),
                onPermissionToggle = {}
            )
        }
    }
}
