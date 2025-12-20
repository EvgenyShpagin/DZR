package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDark
@Composable
private fun CoverMarqueeItem_Size96dp_Preview() {
    DzrTheme {
        Surface {
            CoverMarqueeItem(
                item = CoverMarqueeItemUiState(
                    imageUrl = "",
                    dominantColor = previewImageDominantColor
                ),
                placeholder = previewImagePainter,
                modifier = Modifier.size(96.dp)
            )
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun CoverMarqueeItem_64dpBigGlow_Preview() {
    DzrTheme {
        Surface {
            CoverMarqueeItem(
                item = CoverMarqueeItemUiState(
                    imageUrl = "",
                    dominantColor = previewImageDominantColor
                ),
                glowRadius = 32.dp,
                placeholder = previewImagePainter,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}
