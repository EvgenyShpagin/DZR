package com.music.dzr.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.music.dzr.core.designsystem.theme.DzrTheme

@Composable
fun DzrIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    FilledIconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    ) {
        icon()
    }
}

@PreviewLightDark
@Composable
private fun DzrIconButtonPreview() {
    DzrTheme {
        DzrIconButton(onClick = {})
        { Icon(Icons.Default.Call, null) }
    }
}

@Composable
fun DzrIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    checkedIcon: @Composable () -> Unit = icon
) {
    FilledIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
    ) {
        if (checked) checkedIcon() else icon()
    }
}

@PreviewLightDark
@Composable
private fun DzrIconToggleButtonCheckedPreview() {
    DzrTheme {
        DzrIconToggleButton(
            checked = true,
            onCheckedChange = {},
            icon = {
                Icon(Icons.Default.FavoriteBorder, null)
            },
            checkedIcon = {
                Icon(Icons.Default.Favorite, null)
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun DzrIconToggleButtonUncheckedPreview() {
    DzrTheme {
        DzrIconToggleButton(
            checked = false,
            onCheckedChange = {},
            icon = {
                Icon(Icons.Default.FavoriteBorder, null)
            },
            checkedIcon = {
                Icon(Icons.Default.Favorite, null)
            }
        )
    }
}
