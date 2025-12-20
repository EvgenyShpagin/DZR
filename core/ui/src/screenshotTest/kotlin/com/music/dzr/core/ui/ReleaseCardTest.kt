package com.music.dzr.core.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.component.PreviewLightDarkAndFontScale
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.model.ReleaseType

@PreviewTest
@PreviewLightDarkAndFontScale
@Composable
private fun ReleaseCard_ShortTitle_Preview() {
    DzrTheme {
        Surface {
            ReleaseCard(
                title = "Born in the U.S.A.",
                coverUrl = "https://example.com/cover.jpg",
                onClick = {},
                onLongClick = {},
                releaseYear = "1984",
                explicit = false,
                releaseType = ReleaseType.ALBUM,
                mainArtistName = "Bruce Springsteen"
            )
        }
    }
}

@PreviewTest
@Preview
@Composable
private fun ReleaseCard_LongTitle_Preview() {
    DzrTheme {
        Surface {
            ReleaseCard(
                title = "The Dark Side of the Moon (50th Anniversary Edition)",
                coverUrl = "https://example.com/cover.jpg",
                onClick = {},
                onLongClick = {},
                releaseYear = "2023",
                explicit = true,
                releaseType = ReleaseType.ALBUM,
                mainArtistName = "Pink Floyd"
            )
        }
    }
}
