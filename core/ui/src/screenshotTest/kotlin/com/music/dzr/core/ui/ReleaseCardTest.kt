package com.music.dzr.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.model.ReleaseType

@PreviewTest
@PreviewLightDark
@Composable
private fun ReleaseCard_LongTitle_Preview() {
    DzrTheme {
        ReleaseCard(
            title = "The Dark Side of the Moon (50th Anniversary Edition)",
            coverUrl = "https://example.com/cover.jpg",
            onClick = {},
            onLongClick = {},
            releaseYear = "2023",
            explicit = true,
            releaseType = ReleaseType.ALBUM
        )
    }
}

@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun ReleaseCard_FontScale_1_5_Preview() {
    DzrTheme {
        ReleaseCard(
            title = "Born in the U.S.A.",
            coverUrl = "https://example.com/cover.jpg",
            onClick = {},
            onLongClick = {},
            releaseYear = "1984",
            explicit = false,
            releaseType = ReleaseType.ALBUM
        )
    }
}
