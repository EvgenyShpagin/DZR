package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.music.dzr.core.designsystem.component.DzrIconToggleButton
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@Composable
internal fun PermissionsCheckAllButton(
    onClick: () -> Unit,
    areAllChecked: Boolean,
    modifier: Modifier = Modifier
) {
    DzrIconToggleButton(
        checked = areAllChecked,
        onCheckedChange = { onClick() },
        modifier = modifier,
        icon = {
            Icon(
                imageVector = DzrIcons.DoneAll,
                contentDescription = null
            )
        },
        checkedIcon = {
            Icon(
                imageVector = DzrIcons.RemoveDone,
                contentDescription = null
            )
        }
    )
}

@PreviewLightDark
@Composable
private fun PermissionsCheckAllButtonCheckedPreview() {
    DzrTheme {
        PermissionsCheckAllButton(
            onClick = {},
            areAllChecked = true
        )
    }
}

@PreviewLightDark
@Composable
private fun PermissionsCheckAllButtonUncheckedPreview() {
    DzrTheme {
        PermissionsCheckAllButton(
            onClick = {},
            areAllChecked = false
        )
    }
}
