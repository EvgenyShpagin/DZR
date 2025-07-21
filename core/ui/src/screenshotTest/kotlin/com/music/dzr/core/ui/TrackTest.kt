package com.music.dzr.core.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@Preview
@Composable
private fun Track_ShortText_Preview() {
    val uiState = TrackUiState(
        coverUrl = "",
        title = "Track Title",
        isExplicit = true,
        contributors = listOf("Artist One", "Artist Two")
    )
    DzrTheme {
        Surface {
            Track(state = uiState, onClick = {}, onMoreClick = {})
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun Track_LongText_Preview() {
    val uiState = TrackUiState(
        coverUrl = "",
        title = "This is a Very, Very, Very Long Track Title That Should Definitely Overflow",
        isExplicit = false,
        contributors = listOf("A Very Long Artist Name", "Another Extremely Long Collaborator Name")
    )
    DzrTheme {
        Surface {
            Track(state = uiState, onClick = {}, onMoreClick = {})
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun Track_NoCover_Preview() {
    val uiState = TrackUiState(
        coverUrl = null,
        title = "Track Without a Cover",
        isExplicit = false,
        contributors = listOf("Artist Three")
    )
    DzrTheme {
        Surface {
            Track(state = uiState, onClick = {}, onMoreClick = {})
        }
    }
}

