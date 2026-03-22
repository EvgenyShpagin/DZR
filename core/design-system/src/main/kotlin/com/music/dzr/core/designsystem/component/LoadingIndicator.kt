package com.music.dzr.core.designsystem.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.theme.DzrTheme

/**
 * An indeterminate loading indicator that renders a row of vertical bars
 * oscillating in height and opacity, creating an audio-equalizer-like
 * breathing animation.
 *
 * The component reports [indeterminate progress semantics][progressSemantics]
 * for accessibility.
 *
 * @param modifier Modifier applied to the indicator.
 * @param color Fill color for every bar.
 * @param cycleDurationMillis Duration of one full expand-then-collapse cycle.
 *   Each bar reaches its peak at a staggered offset, so the visual "wave"
 *   travels across the indicator over this period.
 * @param minAlpha Lowest opacity a bar can have (at [minScale]). Must be ≥ 0.
 * @param minScale Lowest height scale factor. Must be ≥ 0.
 * @param barFractions Relative maximum-height fractions for each bar.
 *   Must contain at least 2 values, each in `0f..1f`.
 *   The list length determines the bar count.
 */
@Composable
fun DzrLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = DzrLoadingIndicatorDefaults.color,
    cycleDurationMillis: Int = DzrLoadingIndicatorDefaults.CYCLE_DURATION_MS,
    minAlpha: Float = DzrLoadingIndicatorDefaults.MIN_ALPHA,
    minScale: Float = DzrLoadingIndicatorDefaults.MIN_SCALE,
    barFractions: List<Float> = DzrLoadingIndicatorDefaults.barFractions
) {
    require(minAlpha >= 0f) { "minAlpha must be ≥ 0, was $minAlpha" }
    require(minScale >= 0f) { "minScale must be ≥ 0, was $minScale" }
    require(barFractions.count() >= 2 && barFractions.all { it in 0f..1f }) {
        "barFractions must have ≥ 2 elements, each in 0f..1f"
    }

    val barCount = barFractions.count()
    val transition = rememberInfiniteTransition(label = "LoadingIndicator")
    val collapseDuration = cycleDurationMillis / 2
    val scaleStates = List(barCount) { index ->
        transition.animateFloat(
            initialValue = minScale,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(collapseDuration, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(
                    offsetMillis = index * collapseDuration / barCount,
                    offsetType = StartOffsetType.FastForward
                )
            ),
            label = "scale_$index"
        )
    }

    Spacer(
        modifier = modifier
            .progressSemantics()
            .size(DzrLoadingIndicatorDefaults.compactSize)
            .drawWithCache {
                val elementWidth = size.width / (barCount * 2 - 1)
                val stepX = elementWidth * 2
                val maxHeight = size.height
                val halfMaxHeight = maxHeight / 2f
                val cornerRadius = CornerRadius(elementWidth / 2)
                val alphaRange = 1f - minAlpha

                onDrawBehind {
                    for (index in barFractions.indices) {
                        val scale = scaleStates[index].value
                        val barHeight = maxHeight * barFractions[index] * scale

                        drawRoundRect(
                            color = color,
                            topLeft = Offset(
                                x = index * stepX,
                                y = halfMaxHeight - barHeight / 2f,
                            ),
                            size = Size(elementWidth, barHeight),
                            cornerRadius = cornerRadius,
                            alpha = minAlpha + alphaRange * scale,
                        )
                    }
                }
            }
    )
}

/**
 * Default values for [DzrLoadingIndicator].
 */
object DzrLoadingIndicatorDefaults {

    /** Recommended indicator size for compact window size class. */
    val compactSize = DpSize(width = 80.dp, height = 40.dp)

    /** Recommended indicator size for expanded window size class. */
    val expandedSize = DpSize(width = 96.dp, height = 48.dp)

    /** Primary theme color used for the bars. */
    val color: Color @Composable get() = MaterialTheme.colorScheme.primary

    /**
     * Duration of one full animation cycle (collapse → expand) in milliseconds.
     *
     * Each bar animates from minimal scale to `1f` over half this duration,
     * then reverses back, producing a continuous breathing effect.
     */
    const val CYCLE_DURATION_MS: Int = 2400

    /** Minimum opacity of a bar at its smallest scale. */
    const val MIN_ALPHA: Float = 0.35f

    /** Minimum vertical scale factor of a bar (`0f`–`1f`). */
    const val MIN_SCALE: Float = 0.25f

    /**
     * Relative height fractions for each bar (`0f`–`1f`), defining the
     * static "envelope" shape of the indicator. The array length determines
     * the number of bars drawn.
     *
     * Default produces a symmetrical diamond-like silhouette with 9 bars.
     */
    val barFractions: List<Float> =
        listOf(0.15f, 0.35f, 0.70f, 0.90f, 1.00f, 0.90f, 0.70f, 0.35f, 0.15f)
}

@PreviewLightDark
@Composable
private fun DzrLoadingIndicatorNormalSizePreview() {
    DzrTheme {
        DzrLoadingIndicator(
            modifier = Modifier.size(DzrLoadingIndicatorDefaults.compactSize)
        )
    }
}

@PreviewLightDark
@Composable
private fun DzrLoadingIndicatorExtendedSizePreview() {
    DzrTheme {
        DzrLoadingIndicator(
            modifier = Modifier.size(DzrLoadingIndicatorDefaults.expandedSize)
        )
    }
}
