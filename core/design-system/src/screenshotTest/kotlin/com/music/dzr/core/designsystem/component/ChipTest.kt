package com.music.dzr.core.designsystem.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrFilterChip_Preview() {
    DzrTheme {
        DzrFilterChip(selected = true, onSelectChange = {}) {
            Text("Chip")
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrAssistChip_Preview() {
    DzrTheme {
        DzrAssistChip(onClick = {}) {
            Text("Chip")
        }
    }
}

@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun DzrFilterChip_FontScale_1_5_Preview() {
    DzrTheme {
        DzrFilterChip(selected = true, onSelectChange = {}) {
            Text("Chip With Large Font")
        }
    }
}

@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun DzrAssistChip_FontScale_1_5_Preview() {
    DzrTheme {
        DzrAssistChip(onClick = {}) {
            Text("Chip With Large Font")
        }
    }
}