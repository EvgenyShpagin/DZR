package com.music.dzr.core.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.component.PreviewLightDarkAndFontScale
import com.music.dzr.core.designsystem.theme.DzrTheme


@PreviewTest
@PreviewLightDarkAndFontScale
@Composable
private fun SectionHeader_Preview() {
    DzrTheme {
        Surface {
            SectionHeader(labelRes = android.R.string.untitled)
        }
    }
}

@PreviewTest
@PreviewLightDarkAndFontScale
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

