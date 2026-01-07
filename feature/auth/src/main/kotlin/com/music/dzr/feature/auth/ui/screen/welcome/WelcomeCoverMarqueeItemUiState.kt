package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Model representing a release cover in the marquee.
 *
 * @property imageUrl The URL of the album art.
 * @property dominantColor The dominant color extracted from the album art, used for the glow effect.
 *                         Calculated dynamically if not specified.
 */
@Immutable
data class WelcomeCoverMarqueeItemUiState(
    val imageUrl: String,
    val dominantColor: Color = Color.Unspecified,
)