package com.music.dzr.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDark
@Composable
private fun ArtistCard_LongName_Preview() {
    DzrTheme {
        ArtistCard(
            name = "An Artist With A Very Very Extraordinarily Long Name That Should Be Ellipsized",
            pictureUrl = "",
            onClick = {},
            onLongClick = {}
        )
    }
}

@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun ArtistCard_FontScale_1_5_Preview() {
    DzrTheme {
        ArtistCard(
            name = "Artist (Large Font)",
            pictureUrl = "",
            onClick = {},
            onLongClick = {}
        )
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun ArtistCard_ShortName_Preview() {
    DzrTheme {
        ArtistCard(
            name = "XYZ",
            pictureUrl = "",
            onClick = {},
            onLongClick = {}
        )
    }
}