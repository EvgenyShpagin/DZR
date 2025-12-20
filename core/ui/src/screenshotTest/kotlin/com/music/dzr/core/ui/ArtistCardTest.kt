package com.music.dzr.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.component.PreviewLightDarkAndFontScale
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDarkAndFontScale
@Composable
private fun ArtistCard_ShortName_Preview() {
    DzrTheme {
        ArtistCard(
            name = "Artist",
            pictureUrl = "",
            onClick = {},
            onLongClick = {}
        )
    }
}

@PreviewTest
@Preview
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
