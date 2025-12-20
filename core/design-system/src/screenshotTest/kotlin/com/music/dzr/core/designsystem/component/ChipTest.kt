package com.music.dzr.core.designsystem.component

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDarkAndFontScale
@Composable
private fun DzrFilterChip_Selected_Preview() {
    DzrTheme {
        DzrFilterChip(
            selected = true,
            onSelectChange = {}
        ) {
            Text("Chip")
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrFilterChip_NotSelected_Preview() {
    DzrTheme {
        DzrFilterChip(
            selected = true,
            onSelectChange = {}
        ) {
            Text("Chip")
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrFilterChip_Disabled_Preview() {
    DzrTheme {
        Surface {
            DzrFilterChip(
                selected = true,
                onSelectChange = {},
                enabled = false
            ) {
                Text("Chip")
            }
        }
    }
}

@PreviewTest
@PreviewLightDarkAndFontScale
@Composable
private fun DzrAssistChip_Enabled_Preview() {
    DzrTheme {
        Surface {
            DzrAssistChip(onClick = {}) {
                Text("Chip")
            }
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrAssistChip_Disabled_Preview() {
    DzrTheme {
        Surface {
            DzrAssistChip(
                onClick = {},
                enabled = false
            ) {
                Text("Chip")
            }
        }
    }
}
