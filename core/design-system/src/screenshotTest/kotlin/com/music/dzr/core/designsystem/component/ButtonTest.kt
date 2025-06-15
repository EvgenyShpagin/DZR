package com.music.dzr.core.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun DzrButton_FontScale_1_5_Preview() {
    DzrTheme {
        DzrButton(
            onClick = {},
            enabled = true,
            text = {
                Text("Filled button")
            }
        )
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrButton_Icon_MultipleThemes_Preview() {
    DzrTheme {
        DzrButton(
            onClick = {},
            enabled = true,
            text = {
                Text("Filled button")
            },
            leadingIcon = {
                Icon(DzrIcons.PlayArrow, null)
            }
        )
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrOutlineButton_Icon_MultipleThemes_Preview() {
    DzrTheme {
        DzrOutlinedButton(
            onClick = {},
            enabled = true,
            text = {
                Text("Outlined button")
            },
            leadingIcon = {
                Icon(DzrIcons.PlayArrow, null)
            }
        )
    }
}