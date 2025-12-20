package com.music.dzr.core.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@ThemeAndFontScalePreviews
@Composable
private fun DzrButton_Enabled_Preview() {
    DzrTheme {
        DzrButton(
            onClick = {},
            enabled = true,
            text = { Text("Filled Button") },
            leadingIcon = { Icon(DzrIcons.PlayArrow, null) }
        )
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrButton_Disabled_Preview() {
    DzrTheme {
        Surface {
            DzrButton(
                onClick = {},
                enabled = false,
                text = { Text("Disabled Button") },
                leadingIcon = { Icon(DzrIcons.PlayArrow, null) }
            )
        }
    }
}

@PreviewTest
@Preview
@Composable
private fun DzrButton_Enabled_LongText_Preview() {
    DzrTheme {
        DzrButton(
            onClick = {},
            text = {
                // Check truncation/wrapping behavior
                Text("Very long button text that might overflow available space")
            },
            leadingIcon = { Icon(DzrIcons.PlayArrow, null) }
        )
    }
}

@PreviewTest
@Preview
@Composable
private fun DzrButton_Enabled_NoIcon_Preview() {
    DzrTheme {
        DzrButton(
            onClick = {},
            text = { Text("Text Only") }
        )
    }
}

@PreviewTest
@ThemeAndFontScalePreviews
@Composable
private fun DzrOutlinedButton_Enabled_Preview() {
    DzrTheme {
        Surface {
            DzrOutlinedButton(
                onClick = {},
                enabled = true,
                text = { Text("Outlined Button") },
                leadingIcon = { Icon(DzrIcons.PlayArrow, null) }
            )
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrOutlinedButton_Disabled_Preview() {
    DzrTheme {
        Surface {
            DzrOutlinedButton(
                onClick = {},
                enabled = false,
                text = { Text("Disabled Button") },
                leadingIcon = { Icon(DzrIcons.PlayArrow, null) }
            )
        }
    }
}

@PreviewTest
@Preview
@Composable
private fun DzrOutlinedButton_Enabled_LongText_Preview() {
    DzrTheme {
        Surface {
            DzrOutlinedButton(
                onClick = {},
                text = {
                    // Check truncation/wrapping behavior
                    Text("Very long button text that might overflow available space")
                },
                leadingIcon = { Icon(DzrIcons.PlayArrow, null) }
            )
        }
    }
}

@PreviewTest
@Preview
@Composable
private fun DzrOutlinedButton_Enabled_NoIcon_Preview() {
    DzrTheme {
        Surface {
            DzrOutlinedButton(
                onClick = {},
                text = { Text("Text Only") }
            )
        }
    }
}
