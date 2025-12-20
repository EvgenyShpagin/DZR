package com.music.dzr.core.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.component.PreviewLightDarkAndFontScale
import com.music.dzr.core.designsystem.theme.DzrTheme


@PreviewTest
@PreviewLightDarkAndFontScale
@Composable
private fun PlaylistCard_ShortName_Preview() {
    DzrTheme {
        Surface {
            PlaylistCard(
                name = "My Awesome Mix",
                pictureUrl = "",
                onClick = {},
                onLongClick = {}
            )
        }
    }
}

@PreviewTest
@Preview
@Composable
private fun PlaylistCard_LongName_Preview() {
    DzrTheme {
        Surface {
            PlaylistCard(
                name = "My Super Duper Long Playlist Name That Will Not Fit",
                pictureUrl = "",
                onClick = {},
                onLongClick = {}
            )
        }
    }
}

