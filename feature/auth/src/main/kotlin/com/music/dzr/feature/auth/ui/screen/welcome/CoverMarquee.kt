package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.component.ImagePlaceholder
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.feature.auth.R
import kotlinx.coroutines.launch

/**
 * A marquee carousel displaying a double row of release covers that scroll infinitely.
 *
 * The upper row scrolls in one direction, while the lower row scrolls in the opposite direction.
 *
 * @param items List of [CoverMarqueeItemUiState] to display.
 *  Ideally, pass enough items to fill the screen width twice.
 * @param modifier [Modifier] to be applied to the layout.
 * @param itemSize The size of each item in the marquee.
 * @param itemSpacing The spacing between items in the marquee.
 * @param velocity The scrolling velocity in dp/second.
 * @param itemLimit The limit on the number of items to render for the infinite scroll simulation.
 * @param animationDelayMs Delay in milliseconds before the scrolling animation starts.
 * @param placeholder Placeholder [Painter] to display while images are loading or if usage fails.
 */
@Composable
internal fun CoverMarquee(
    items: List<CoverMarqueeItemUiState>,
    modifier: Modifier = Modifier,
    itemSize: Dp = CoverMarqueeDefaults.ItemSize,
    itemSpacing: Dp = CoverMarqueeDefaults.ItemSpacing,
    velocity: Dp = CoverMarqueeDefaults.Velocity,
    itemLimit: Int = CoverMarqueeDefaults.ITEM_LIMIT,
    animationDelayMs: Int = 1000,
    placeholder: Painter = ImagePlaceholder
) {
    if (items.isEmpty()) return

    val isDarkTheme = isSystemInDarkTheme()
    val arrangement = Arrangement.spacedBy(itemSpacing)
    val itemModifier = Modifier.size(itemSize)

    // Independent states for independent scroll control
    val upperRowState = rememberLazyListState()
    val lowerRowState = rememberLazyListState()

    val density = LocalDensity.current

    LaunchedEffect(items, density, velocity, itemLimit) {
        launch {
            upperRowState.animateScrollToEnd(
                density = density,
                initialDelayMs = animationDelayMs,
                itemSize = itemSize,
                itemSpacing = itemSpacing,
                totalItemsCount = itemLimit,
                velocity = velocity
            )
        }
        launch {
            // Align the lower row (reverse layout) to visually match the desired starting position
            lowerRowState.alignEdgeVisibleToOppositeSide()

            lowerRowState.animateScrollToEnd(
                density = density,
                initialDelayMs = animationDelayMs,
                itemSize = itemSize,
                itemSpacing = itemSpacing,
                totalItemsCount = itemLimit,
                velocity = velocity
            )
        }
    }

    val halfUrlCount = items.count() / 2

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = arrangement
    ) {
        LazyRow(
            horizontalArrangement = arrangement,
            state = upperRowState,
            userScrollEnabled = false,
            contentPadding = PaddingValues(horizontal = itemSpacing)
        ) {
            items(count = itemLimit) { index ->
                val sourceIndex = index % halfUrlCount
                val item = items[sourceIndex]

                CoverMarqueeItem(
                    item = item,
                    placeholder = placeholder,
                    isBright = !isDarkTheme,
                    modifier = itemModifier
                )
            }
        }

        LazyRow(
            horizontalArrangement = arrangement,
            state = lowerRowState,
            userScrollEnabled = false,
            reverseLayout = true,
            contentPadding = PaddingValues(horizontal = itemSpacing)
        ) {
            items(count = itemLimit) { index ->
                // Offset the index to show the second half of items primarily
                val sourceIndex = (index % halfUrlCount + halfUrlCount) % items.count()
                val item = items[sourceIndex]

                CoverMarqueeItem(
                    item = item,
                    placeholder = placeholder,
                    isBright = !isDarkTheme,
                    modifier = itemModifier
                )
            }
        }
    }
}

/**
 * Scrolls the list to align the content edge to the opposite side of the viewport.
 * Useful for [LazyRow] with `reverseLayout = true` to make it look left-aligned (or strictly speaking,
 * to start the content "off-screen" or at the edge in a way that matches the other row).
 */
private suspend fun LazyListState.alignEdgeVisibleToOppositeSide() {
    val visibleItems = layoutInfo.visibleItemsInfo
    if (visibleItems.isNotEmpty()) {
        val contentWidth = visibleItems.sumOf { it.size } +
                (visibleItems.size - 1).coerceAtLeast(0) * layoutInfo.mainAxisItemSpacing +
                layoutInfo.beforeContentPadding + layoutInfo.afterContentPadding

        val offset = contentWidth - layoutInfo.viewportSize.width
        scrollBy(offset.toFloat())
    }
}

/**
 * Scrolls the list linearly to the end.
 *
 * @param density The current [Density] for px/dp conversion.
 * @param initialDelayMs Initial delay before animation starts.
 * @param itemSize The size of each item (width for LazyRow).
 * @param itemSpacing The spacing between items.
 * @param totalItemsCount The total number of items in the list.
 * @param velocity The scrolling velocity in dp/second.
 */
private suspend fun LazyListState.animateScrollToEnd(
    density: Density,
    initialDelayMs: Int,
    itemSize: Dp,
    itemSpacing: Dp,
    totalItemsCount: Int,
    velocity: Dp
) {
    if (totalItemsCount == 0) return

    val totalLengthDp = (itemSize + itemSpacing) * totalItemsCount - itemSpacing
    val totalLengthPx = with(density) { totalLengthDp.toPx() }
    val durationMillis = (totalLengthDp.value / velocity.value * 1000).toLong()

    animateScrollBy(
        value = totalLengthPx,
        animationSpec = tween(
            durationMillis = durationMillis.toInt(),
            delayMillis = initialDelayMs,
            easing = LinearEasing
        )
    )
}

internal object CoverMarqueeDefaults {
    val ItemSize = 128.dp
    val ItemSpacing = 16.dp
    val Velocity = 5.dp
    const val ITEM_LIMIT = 100
}

@PreviewLightDark
@Composable
private fun CoverMarqueePreview() {
    DzrTheme {
        Surface {
            Box(Modifier.padding(vertical = 16.dp)) {
                CoverMarquee(
                    items = previewItems,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = painterResource(R.drawable.feature_auth_preview_release_cover)
                )
            }
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
