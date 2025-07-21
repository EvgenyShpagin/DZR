package com.music.dzr.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import com.music.dzr.core.designsystem.component.DzrIconButton
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme
import kotlin.math.max

/**
 * A customizable header component with play controls.
 * Combines titles, a dynamic play button, and custom content in an adaptive layout.
 *
 * Uses [SubcomposeLayout] for optimized measurement of complex content.
 *
 * @param title String for main title
 * @param onPlayClick Callback when play button is clicked
 * @param isPlaying Whether tracklist is currently playing or not
 * @param modifier The [Modifier] to be applied to this header
 * @param subtitle Optional string for subtitle
 * @param playButtonSize Maximum size of the play button (default: [PlayableHeaderDefaults.PlayButtonSize])
 * @param buttonSpacing Space between text content and play button (default: [PlayableHeaderDefaults.ButtonSpacing])
 * @param horizontalMargin Horizontal padding values (default: [PlayableHeaderDefaults.Margin])
 * @param minButtonSize Minimum play button size if it doesn't fit completely (default: [PlayableHeaderDefaults.MinButtonSize])
 * @param windowInsets Window insets to handle (default: [PlayableHeaderDefaults.windowInsets])
 * @param content Slot API for custom content below titles. Receives [PlayableHeaderLayout] with:
 * - innerSpaceHeight: Available vertical space between titles and bottom
 * - widthExcludePlayButton: Available width excluding play button area
 * - isButtonDisplayed: Whether play button is currently visible
 *
 * @see PlayableHeaderLayout For layout parameters passed to content slot
 * @see PlayableHeaderDefaults For default style values
 */
@Composable
fun PlayableHeader(
    title: String,
    onPlayClick: () -> Unit,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    playButtonSize: Dp = PlayableHeaderDefaults.PlayButtonSize,
    buttonSpacing: Dp = PlayableHeaderDefaults.ButtonSpacing,
    horizontalMargin: PaddingValues = PlayableHeaderDefaults.Margin,
    minButtonSize: Dp = PlayableHeaderDefaults.MinButtonSize,
    windowInsets: WindowInsets = PlayableHeaderDefaults.windowInsets,
    content: @Composable (PlayableHeaderLayout) -> Unit
) {
    PlayableHeaderLayout(
        modifier = modifier
            .padding(horizontalMargin)
            .consumeWindowInsets(horizontalMargin)
            .windowInsetsPadding(windowInsets),
        title = title,
        subtitle = subtitle,
        isPlaying = isPlaying,
        onPlayClick = onPlayClick,
        playButtonSize = playButtonSize,
        buttonSpacing = buttonSpacing,
        minButtonSize = minButtonSize,
        content = content
    )
}

/**
 * Layout for a [PlayableHeader]'s content.
 *
 * @param title the main title to display
 * @param subtitle optional subtitle to display below the title
 * @param isPlaying whether the content is currently playing
 * @param onPlayClick callback when the play button is clicked
 * @param playButtonSize maximum size of the play button
 * @param buttonSpacing space between text content and play button
 * @param minButtonSize minimum play button size
 * @param content the main content slot of the header
 */
@Composable
private fun PlayableHeaderLayout(
    title: String,
    subtitle: String?,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    playButtonSize: Dp,
    buttonSpacing: Dp,
    minButtonSize: Dp,
    content: @Composable (PlayableHeaderLayout) -> Unit,
    modifier: Modifier = Modifier
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val playButtonSizePx = playButtonSize.roundToPx()
        val minButtonSizePx = minButtonSize.roundToPx()
        val buttonSpacingPx = buttonSpacing.roundToPx()

        // Measure title
        val titlePlaceable = subcompose(PlayableHeaderLayoutContent.Title) {
            PlayableHeaderTitle(title = title)
        }.single().measure(looseConstraints)

        val titleWidth = titlePlaceable.width

        // Determine if play button should be displayed
        val availableButtonWidthPx = (layoutWidth - titleWidth - buttonSpacingPx)
            .coerceAtMost(playButtonSizePx)
        val shouldDisplayPlayButton = availableButtonWidthPx >= minButtonSizePx

        val maxSubtitleWidth = layoutWidth - availableButtonWidthPx - buttonSpacingPx

        // Measure subtitle
        val subtitlePlaceable = subcompose(PlayableHeaderLayoutContent.Subtitle) {
            if (subtitle != null) {
                PlayableHeaderSubtitle(subtitle = subtitle)
            }
        }.singleOrNull()?.measure(looseConstraints.copy(maxWidth = maxSubtitleWidth))

        val titlesHeight = titlePlaceable.height + (subtitlePlaceable?.height ?: 0)

        // Measure play button if it should be displayed
        val playButtonPlaceables = subcompose(PlayableHeaderLayoutContent.PlayButton) {
            if (shouldDisplayPlayButton) {
                PlayableHeaderButton(
                    isPlaying = isPlaying,
                    onClick = onPlayClick,
                    modifier = Modifier.size(availableButtonWidthPx.toDp())
                )
            }
        }.fastMap {
            it.measure(Constraints.fixed(availableButtonWidthPx, availableButtonWidthPx))
        }

        val playButtonWidth = playButtonPlaceables.fastMaxBy { it.width }?.width ?: 0
        val playButtonHeight = playButtonPlaceables.fastMaxBy { it.height }?.height ?: 0

        // Calculate available space for main content
        val contentMaxWidth = layoutWidth - if (shouldDisplayPlayButton) playButtonWidth else 0
        val contentMaxHeight = if (constraints.hasFixedHeight) {
            (layoutHeight - titlesHeight).coerceAtLeast(0)
        } else {
            Constraints.Infinity
        }

        val innerSpaceHeight = max(0.dp, playButtonHeight.toDp() - titlesHeight.toDp())

        val layoutData = PlayableHeaderLayout(
            innerSpaceHeight = innerSpaceHeight,
            widthExcludePlayButton = contentMaxWidth.toDp(),
            isButtonDisplayed = shouldDisplayPlayButton
        )

        // Measure main content
        val contentPlaceables = subcompose(PlayableHeaderLayoutContent.MainContent) {
            content(layoutData)
        }.fastMap {
            it.measure(
                looseConstraints.copy(
                    maxWidth = contentMaxWidth,
                    maxHeight = contentMaxHeight
                )
            )
        }

        val contentHeight = contentPlaceables.fastMaxBy { it.height }?.height ?: 0

        // Calculate final layout dimensions
        val calculatedHeight = max(titlesHeight + contentHeight, playButtonHeight)
        val finalHeight = if (constraints.hasFixedHeight) layoutHeight else calculatedHeight

        layout(layoutWidth, finalHeight) {
            // Place titles
            titlePlaceable.placeRelative(0, 0)
            if (subtitlePlaceable != null) {
                val titleHeight = titlePlaceable.height
                subtitlePlaceable.placeRelative(0, titleHeight)
            }

            // Place main content below titles if there's space
            if (finalHeight - titlesHeight > 0) {
                contentPlaceables.forEach { it.placeRelative(0, titlesHeight) }
            }

            // Place play button at the end
            playButtonPlaceables.forEach {
                it.placeRelative(layoutWidth - it.width, 0)
            }
        }
    }
}

@Composable
private fun PlayableHeaderTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.displayLarge,
        modifier = modifier
    )
}

@Composable
private fun PlayableHeaderSubtitle(
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = subtitle,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
    )
}

@Composable
private fun PlayableHeaderButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DzrIconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        if (isPlaying) {
            Icon(
                imageVector = DzrIcons.Pause,
                contentDescription = stringResource(R.string.core_ui_cd_pause_music)
            )
        } else {
            Icon(
                imageVector = DzrIcons.PlayArrow,
                contentDescription = stringResource(R.string.core_ui_cd_play_music)
            )
        }
    }
}

/**
 * Layout parameters provided to [PlayableHeader]'s content slot.
 *
 * @property innerSpaceHeight Available vertical space between subtitle baseline and bottom of the button
 * @property widthExcludePlayButton Available width excluding the play button area
 * @property isButtonDisplayed Whether the play button is currently visible
 */
@Immutable
data class PlayableHeaderLayout(
    val innerSpaceHeight: Dp,
    val widthExcludePlayButton: Dp,
    val isButtonDisplayed: Boolean
)

/** Object containing various default values for [PlayableHeader] styling. */
object PlayableHeaderDefaults {
    /** Default size for play button */
    val PlayButtonSize: Dp = 128.dp

    /** Default spacing between text and button */
    val ButtonSpacing: Dp = 8.dp

    /** Minimum play button size */
    val MinButtonSize: Dp = 56.dp

    /** Default horizontal margins around header */
    val Margin: PaddingValues = PaddingValues(horizontal = 16.dp)

    /** Default window insets to be handled by the header */
    val windowInsets: WindowInsets
        @Composable get() = WindowInsets.systemBars.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Top
        )
}

private enum class PlayableHeaderLayoutContent {
    Title,
    Subtitle,
    PlayButton,
    MainContent
}

@Preview
@Composable
private fun PlayableHeader_WithSubtitle_Preview() {
    DzrTheme {
        PlayableHeader(
            title = "Title",
            subtitle = "Subtitle Subtitle Subtitle Subtitle Subtitle Subtitle Subtitle Subtitle.....",
            onPlayClick = {},
            isPlaying = false
        ) { layout ->
            Box(
                Modifier
                    .background(Color.LightGray)
                    .height(layout.innerSpaceHeight)
                    .width(layout.widthExcludePlayButton)
            ) {
                Text("Filled space")
            }
        }
    }
}

@Preview
@Composable
private fun PlayableHeader_LongTitle_Preview() {
    DzrTheme {
        PlayableHeader(
            title = "Some Very Long Title That Should Be Truncated",
            onPlayClick = {},
            isPlaying = true
        ) { layout ->
            Box(
                Modifier
                    .background(Color.LightGray)
                    .height(64.dp)
                    .width(layout.widthExcludePlayButton)
            ) {
                Text("Content")
            }
        }
    }
}

@Preview
@Composable
private fun PlayableHeader_Playing_Preview() {
    DzrTheme {
        PlayableHeader(
            title = "Now Playing",
            subtitle = "Artist Name",
            onPlayClick = {},
            isPlaying = true
        ) { layout ->
            if (layout.isButtonDisplayed) {
                Text("Button is visible")
            } else {
                Text("Button is hidden")
            }
        }
    }
}
