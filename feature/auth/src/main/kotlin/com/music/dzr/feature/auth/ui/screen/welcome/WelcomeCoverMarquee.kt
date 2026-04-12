package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.annotation.VisibleForTesting
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
import androidx.compose.runtime.key
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A marquee carousel displaying multiple rows of release covers that scroll infinitely.
 *
 * Rows one after another move towards each other.
 *
 * @param items List of [WelcomeCoverMarqueeItemUiState] to display.
 *  Ideally, pass enough items to fill the screen width [rowCount] times.
 * @param rowCount The number of animated rows. For optimal performance and readability,
 *  values greater than 2 are not recommended.
 * @param modifier [Modifier] to be applied to the layout.
 * @param itemSize The size of each item in the marquee.
 * @param itemSpacing The spacing between items in the marquee.
 * @param velocity The scrolling velocity in dp/second.
 * @param itemLimit The limit on the number of items to render for the infinite scroll simulation.
 * @param animationDelayMs Delay in milliseconds before the scrolling animation starts.
 * @param placeholder Placeholder [Painter] to display while images are loading or if usage fails.
 */
@Composable
internal fun WelcomeCoverMarquee(
    items: List<WelcomeCoverMarqueeItemUiState>,
    rowCount: Int,
    modifier: Modifier = Modifier,
    itemSize: Dp = WelcomeCoverMarqueeDefaults.ItemSize,
    itemSpacing: Dp = WelcomeCoverMarqueeDefaults.ItemSpacing,
    velocity: Dp = WelcomeCoverMarqueeDefaults.Velocity,
    itemLimit: Int = WelcomeCoverMarqueeDefaults.ITEM_LIMIT,
    animationDelayMs: Int = 1000,
    placeholder: Painter = ImagePlaceholder
) {
    if (items.isEmpty()) return
    require(items.count() / rowCount >= 1) { "The items do not cover all rows" }

    val isDarkTheme = isSystemInDarkTheme()
    val arrangement = Arrangement.spacedBy(itemSpacing)
    val itemModifier = Modifier.size(itemSize)

    // Independent states for independent scroll control
    val rowStates = Array(rowCount) { i -> key(i) { rememberLazyListState() } }

    val density = LocalDensity.current

    LaunchedEffect(items, density, velocity, itemLimit) {
        repeat(rowCount) {
            launch {
                val rowState = rowStates[it]
                if (it % 2 == 1) {
                    rowState.alignEdgeVisibleToOppositeSide()
                }
                rowState.animateScrollToEnd(
                    density = density,
                    initialDelayMs = animationDelayMs,
                    itemSize = itemSize,
                    itemSpacing = itemSpacing,
                    totalItemsCount = itemLimit,
                    velocity = velocity
                )
            }
        }
    }

    val rows = items.distributeIntoRows(rowCount)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = arrangement
    ) {
        repeat(rowCount) {
            val row = rows[it]
            val rowState = rowStates[it]

            LazyRow(
                horizontalArrangement = arrangement,
                state = rowState,
                userScrollEnabled = false,
                reverseLayout = it % 2 == 1,
                contentPadding = PaddingValues(horizontal = itemSpacing)
            ) {
                items(count = itemLimit) { absIndex ->
                    val index = absIndex % row.size
                    val item = row[index]
                    WelcomeCoverMarqueeItem(
                        state = item,
                        placeholder = placeholder,
                        isBright = !isDarkTheme,
                        modifier = itemModifier
                    )
                }
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

private const val NANOS_PER_SECOND = 1_000_000_000.0
private const val MAX_DELTA_TIME_NANOS = 32_000_000L

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

    val pxPerSecond = with(density) { velocity.toPx() }

    delay(initialDelayMs.toLong())

    var scrolledDistance = 0f
    var lastFrameTimeNanos = withFrameNanos { it }

    while (scrolledDistance < totalLengthPx) {
        val frameTimeNanos = withFrameNanos { it }

        var deltaTimeNanos = frameTimeNanos - lastFrameTimeNanos
        lastFrameTimeNanos = frameTimeNanos

        // Prevent animation jumps caused by UI jank by capping deltaTime at ~30 FPS (32ms)
        if (deltaTimeNanos > MAX_DELTA_TIME_NANOS) {
            deltaTimeNanos = MAX_DELTA_TIME_NANOS
        }
        val deltaTimeSeconds = deltaTimeNanos / NANOS_PER_SECOND
        val distanceToMove = (pxPerSecond * deltaTimeSeconds).toFloat()
        val step = minOf(distanceToMove, totalLengthPx - scrolledDistance)
        val consumed = scrollBy(step)
        scrolledDistance += consumed
        if (consumed == 0f) break
    }
}

/**
 * Distributes the items into a specific number of rows, placing any remainder
 * in the last row.
 *
 * Example: Distributing `[1, 2, 3, 4, 5]` into `2` rows results in `[[1, 2], [3, 4, 5]]`.
 *
 * @param rowCount The exact number of rows to create. Must be > 0.
 */
@VisibleForTesting
fun List<WelcomeCoverMarqueeItemUiState>.distributeIntoRows(
    rowCount: Int
): List<List<WelcomeCoverMarqueeItemUiState>> {
    require(rowCount > 0) { "Row count must be greater than 0" }
    val rowItemCount = count() / rowCount
    require(rowItemCount > 0) { "Each row item count must be greater than 0" }
    val lastRowItemCount = rowItemCount + count() % rowCount

    val rows = List(rowCount) { rowIndex ->
        val fromIndex = rowIndex * rowItemCount
        val toIndex = if (rowIndex == rowCount - 1) {
            fromIndex + lastRowItemCount
        } else {
            fromIndex + rowItemCount
        }
        subList(fromIndex, toIndex)
    }
    return rows
}

internal object WelcomeCoverMarqueeDefaults {
    val ItemSize = 128.dp
    val ItemSpacing = 16.dp
    val Velocity = 5.dp
    const val ITEM_LIMIT = 100
}

@PreviewLightDark
@Composable
private fun WelcomeCoverMarqueePreview() {
    DzrTheme {
        Surface {
            Box(Modifier.padding(vertical = 16.dp)) {
                WelcomeCoverMarquee(
                    items = previewCoverMarquees,
                    rowCount = 2,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = painterResource(R.drawable.feature_auth_preview_release_cover)
                )
            }
        }
    }
}
