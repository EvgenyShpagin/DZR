package com.music.dzr.core.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.theme.DzrTheme


@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun SectionHeader_FontScale_1_5_Preview() {
    DzrTheme {
        Surface {
            SectionHeader(labelRes = android.R.string.untitled)
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun ClickableSectionHeader_Preview() {
    DzrTheme {
        Surface {
            ClickableSectionHeader(
                onClick = {},
                labelRes = android.R.string.untitled
            )
        }
    }
}

