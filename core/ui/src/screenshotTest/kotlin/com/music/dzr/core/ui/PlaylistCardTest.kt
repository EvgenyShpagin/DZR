package com.music.dzr.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.theme.DzrTheme


@PreviewTest
@PreviewLightDark
@Composable
private fun PlaylistCard_ShortName_Preview() {
    DzrTheme {
        PlaylistCard(
            name = "My Awesome Mix",
            pictureUrl = "",
            onClick = {},
            onLongClick = {}
        )
    }
}

@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun PlaylistCard_FontScale_1_5_Preview() {
    DzrTheme {
        PlaylistCard(
            name = "My Awesome Mix",
            pictureUrl = "",
            onClick = {},
            onLongClick = {}
        )
    }
}


@PreviewTest
@Preview
@Composable
private fun PlaylistCard_LongName_Preview() {
    DzrTheme {
        PlaylistCard(
            name = "My Super Duper Long Playlist Name That Will Not Fit",
            pictureUrl = "",
            onClick = {},
            onLongClick = {}
        )
    }
}

