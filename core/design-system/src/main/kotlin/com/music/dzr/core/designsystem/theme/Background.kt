package com.music.dzr.core.designsystem.theme

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A data class to hold the colors for the gradient background.
 */
@Immutable
data class GradientBackground(
    val topColor: Color = Color.Unspecified,
    val bottomColor: Color = Color.Unspecified
)

/**
 * A background component that draws a vertical gradient which moves with the scroll state.
 *
 * This component is designed to be placed at the root of a screen. It fills the available size
 * and draws a gradient that starts at the top and transitions to the bottom color.
 * As the user scrolls, the gradient moves up, creating a parallax-like or scroll-aware background effect.
 *
 * @param modifier The modifier to be applied to the Box.
 * @param scrollState The scroll state to observe. If null, the background remains static.
 * @param gradientHeight The height of the gradient transition.
 * @param gradientColors The colors for the gradient.
 * @param content The content to display on top of the background.
 */
@Composable
fun DzrBackground(
    modifier: Modifier = Modifier,
    scrollState: ScrollState? = null,
    gradientHeight: Dp = DzrBackgroundDefaults.gradientHeight,
    gradientColors: GradientBackground = DzrBackgroundDefaults.gradientColors(),
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithCache {
                // Calculate gradient height in pixels
                val gradientHeightPx = with(density) { gradientHeight.toPx() }

                // Create the brush once.
                // It is defined from 0 to gradientHeightPx.
                // We use TileMode.Clamp so that the region below the gradient uses the bottomColor.
                val gradientBrush = Brush.verticalGradient(
                    colors = listOf(gradientColors.topColor, gradientColors.bottomColor),
                    startY = 0f,
                    endY = gradientHeightPx,
                    tileMode = TileMode.Clamp
                )

                onDrawBehind {
                    val scrollValue = scrollState?.value ?: 0
                    // We translate the canvas up by the scroll value.
                    // This moves the gradient start (0,0) to (0, -scrollValue).
                    translate(top = -scrollValue.toFloat()) {
                        // We need to draw a rectangle that covers the current viewport.
                        // Since we translated the coordinates up,
                        // the "visible" viewport starts at y = scrollValue.
                        drawRect(
                            brush = gradientBrush,
                            topLeft = Offset(0f, scrollValue.toFloat()),
                            size = size
                        )
                    }
                }
            }
    ) {
        content()
    }
}

object DzrBackgroundDefaults {
    val gradientHeight = 2000.dp

    private val topColor @Composable get() = MaterialTheme.colorScheme.primaryContainer
    private val bottomColor @Composable get() = MaterialTheme.colorScheme.surface

    @Composable
    fun gradientColors(): GradientBackground {
        return GradientBackground(
            topColor = topColor,
            bottomColor = bottomColor
        )
    }

    @Composable
    fun gradientColors(
        topColor: Color = Color.Unspecified,
        bottomColor: Color = Color.Unspecified
    ): GradientBackground {
        return GradientBackground(
            topColor = topColor.takeOrElse { DzrBackgroundDefaults.topColor },
            bottomColor = bottomColor.takeOrElse { DzrBackgroundDefaults.bottomColor }
        )
    }
}

@PreviewLightDark
@Composable
private fun DzrBackgroundPreview() {
    DzrTheme {
        DzrBackground(
            scrollState = rememberScrollState(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(Modifier.fillMaxSize())
        }
    }
}
