package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class WelcomePreviewParameterProvider : PreviewParameterProvider<List<WelcomeCoverMarqueeItemUiState>> {
    override val values = sequenceOf(
        previewCoverMarquees.subList(0, 1),
        previewCoverMarquees
    )
}