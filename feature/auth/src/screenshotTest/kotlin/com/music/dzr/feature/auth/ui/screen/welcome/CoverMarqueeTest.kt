package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.feature.auth.R

@PreviewTest
@PreviewLightDark
@Composable
private fun CoverMarquee_ItemSize96dp_Preview() {
    DzrTheme {
        Surface {
            CoverMarquee(
                items = previewItems,
                itemSize = 96.dp,
                modifier = Modifier.fillMaxWidth(),
                placeholder = painterResource(R.drawable.feature_auth_preview_release_cover)
            )
        }
    }
}

private val previewItems = listOf(
    CoverMarqueeItemUiState("", Color.Blue),
    CoverMarqueeItemUiState("", Color.Cyan),
    CoverMarqueeItemUiState("", Color.Red),
    CoverMarqueeItemUiState("", Color.Magenta),
    CoverMarqueeItemUiState("", Color.Yellow),
    CoverMarqueeItemUiState("", Color.Green),
)
