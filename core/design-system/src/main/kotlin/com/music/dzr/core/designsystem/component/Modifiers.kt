package com.music.dzr.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * Constrains content to [maxWidth] and centers it horizontally within the parent.
 * When the parent is narrower than [maxWidth], the cap has no effect.
 */
fun Modifier.centeredContent(maxWidth: Dp): Modifier =
    this
        .fillMaxWidth()
        .wrapContentWidth(align = Alignment.CenterHorizontally)
        .widthIn(max = maxWidth)
