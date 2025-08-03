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
private fun ReleaseRow_MultipleContributors_Preview() {
    DzrTheme {
        ReleaseRow(
            title = "Watch the Throne",
            contributors = listOf("Jay-Z", "Kanye West"),
            coverUrl = "https://example.com/cover.jpg",
            onClick = {},
            onMoreClick = {},
            releaseYear = "2011",
            explicit = true,
            releaseType = ReleaseType.ALBUM
        )
    }
}

@PreviewTest
@Preview
@Composable
private fun ReleaseRow_LongTitle_Preview() {
    DzrTheme {
        ReleaseRow(
            title = "The Dark Side of the Moon (50th Anniversary Edition)",
            contributors = listOf("Pink Floyd"),
            coverUrl = "https://example.com/cover.jpg",
            onClick = {},
            onMoreClick = {},
            releaseYear = "2023",
            explicit = false,
            releaseType = ReleaseType.ALBUM
        )
    }
}

@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun ReleaseRow_FontScale_1_5_Preview() {
    DzrTheme {
        ReleaseRow(
            title = "Born in the U.S.A.",
            contributors = listOf("Bruce Springsteen"),
            coverUrl = "https://example.com/cover.jpg",
            onClick = {},
            onMoreClick = {},
            releaseYear = "1984",
            explicit = false,
            releaseType = ReleaseType.ALBUM
        )
    }
}
