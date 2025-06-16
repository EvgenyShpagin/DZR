package com.music.dzr.core.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrIconButton_Preview() {
    DzrTheme {
        DzrIconButton(onClick = {}) {
            Icon(DzrIcons.Favorite, null)
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrIconToggleButton_Checked_Preview() {
    DzrTheme {
        DzrIconToggleButton(
            checked = true,
            onCheckedChange = {},
            icon = {
                Icon(DzrIcons.FavoriteBorder, null)
            },
            checkedIcon = {
                Icon(DzrIcons.Favorite, null)
            }
        )
    }
}

@PreviewTest
@Preview
@Composable
private fun DzrIconToggleButton_Unchecked_Preview() {
    DzrTheme {
        DzrIconToggleButton(
            checked = false,
            onCheckedChange = {},
            icon = {
                Icon(DzrIcons.FavoriteBorder, null)
            },
            checkedIcon = {
                Icon(DzrIcons.Favorite, null)
            }
        )
    }
} 