package com.music.dzr.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.ColorPainter

/**
 * A standard placeholder painter used for images while they are loading or if they fail to load.
 */
val ImagePlaceholder @Composable get() = ColorPainter(MaterialTheme.colorScheme.surfaceContainer)
