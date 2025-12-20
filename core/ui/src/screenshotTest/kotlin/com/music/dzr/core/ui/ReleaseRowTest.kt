package com.music.dzr.core.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.component.ThemeAndFontScalePreviews
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.model.ReleaseType


@PreviewTest
@ThemeAndFontScalePreviews
@Composable
private fun ReleaseRow_MultipleContributors_Preview() {
    DzrTheme {
        Surface {
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
}

@PreviewTest
@Preview
@Composable
private fun ReleaseRow_LongTitle_Preview() {
    DzrTheme {
        Surface {
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
}
