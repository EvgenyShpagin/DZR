package com.music.dzr.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFold
import androidx.compose.ui.util.fastForEach
import com.music.dzr.core.designsystem.component.DzrIconButton
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

/**
 * A customizable header component with play controls.
 * Combines titles, a dynamic play button, and custom content in an adaptive layout.
 *
 * Uses [SubcomposeLayout] for optimized measurement of complex content.
 *
 * @param title String for main title
 * @param onPlayClick Callback when play button is clicked
 * @param isPlaying Whether tracklist is currently playing or not
 * @param modifier Optional [Modifier] for the root layout
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
    SubcomposeLayout(
        modifier = modifier
            .padding(horizontalMargin)
            .consumeWindowInsets(horizontalMargin)
            .windowInsetsPadding(windowInsets)
    ) { constraints ->
        val titlesPlaceables = subcompose(PlayableTopAppBarSlot.Titles) {
            Column {
                DisplayText(title)
                if (subtitle != null) {
                    Subtitle(subtitle)
                }
            }
        }.map { it.measure(constraints) }

        val titlesSize =
            titlesPlaceables.fastFold(initial = IntSize.Zero) { currentMax, placeable ->
                IntSize(
                    width = minOf(currentMax.width, placeable.width),
                    height = currentMax.height + placeable.height
                )
            }
        val titlesWidth = titlesSize.width.toDp()
        val titlesWidthWithSpacing = titlesWidth + buttonSpacing

        val actualPlayButtonSize = (constraints.maxWidth.toDp() - titlesWidthWithSpacing)
            .coerceIn(0.dp..playButtonSize)
        val actualPlayButtonSizePx = actualPlayButtonSize.roundToPx()
        val playButtonFits = actualPlayButtonSize >= minButtonSize

        val widthExcludePlayButton = constraints.maxWidth - actualPlayButtonSizePx

        val contentPlaceables = subcompose(PlayableTopAppBarSlot.MainContent) {

            val resultLayout = PlayableHeaderLayout(
                innerSpaceHeight = (actualPlayButtonSize - titlesSize.height.toDp())
                    .coerceAtLeast(0.dp),
                widthExcludePlayButton = widthExcludePlayButton.toDp(),
                isButtonDisplayed = playButtonFits
            )
            content(resultLayout)

        }.map { it.measure(constraints) }

        val contentSize = contentPlaceables.fastFold(IntSize.Zero) { currentMax, placeable ->
            IntSize(
                width = maxOf(currentMax.width, placeable.width),
                height = maxOf(currentMax.height, placeable.height)
            )
        }

        layout(
            width = constraints.maxWidth,
            height = maxOf(titlesSize.height + contentSize.height, actualPlayButtonSizePx)
        ) {
            titlesPlaceables.fastForEach { it.placeRelative(0, 0) }
            contentPlaceables.fastForEach { it.placeRelative(0, titlesSize.height) }
            if (playButtonFits) {
                subcompose(PlayableTopAppBarSlot.PlayButton) {
                    PlayButton(
                        isPlaying = isPlaying,
                        onPlayClick = onPlayClick,
                        modifier = Modifier.size(actualPlayButtonSize)
                    )
                }.fastForEach {
                    val buttonConstraints = Constraints.fixed(
                        width = actualPlayButtonSizePx,
                        height = actualPlayButtonSizePx
                    )
                    it.measure(buttonConstraints).placeRelative(widthExcludePlayButton, 0)
                }
            }
        }
    }
}

@Composable
private fun DisplayText(title: String) {
    Text(
        text = title,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = MaterialTheme.typography.displayLarge
    )
}

@Composable
private fun Subtitle(subtitle: String) {
    Text(
        text = subtitle,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelLarge
    )
}

@Composable
private fun PlayButton(
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DzrIconButton(
        onClick = onPlayClick,
        modifier = modifier
    ) {
        if (isPlaying) {
            Icon(DzrIcons.Pause, stringResource(R.string.core_ui_cd_pause_music))
        } else {
            Icon(DzrIcons.PlayArrow, stringResource(R.string.core_ui_cd_play_music))
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

private enum class PlayableTopAppBarSlot {
    Titles,
    PlayButton,
    MainContent
}

@Preview
@Composable
private fun PlayableHeader_WithSubtitle_Preview() {
    DzrTheme {
        PlayableHeader(
            title = "Title",
            subtitle = "Subtitle",
            onPlayClick = {},
            isPlaying = false,
            content = { (bottomSpaceHeight, widthExcludePlayButton, _) ->
                Box(
                    Modifier
                        .background(Color.LightGray)
                        .height(bottomSpaceHeight)
                        .width(widthExcludePlayButton)
                ) {
                    Text("Filled space")
                }
            }
        )
    }
}

@Preview
@Composable
private fun PlayableHeader_LongText_WithoutSubtitle_Preview() {
    DzrTheme {
        PlayableHeader(
            title = "Some Long Title",
            onPlayClick = {},
            isPlaying = false,
            content = { (_, widthExcludePlayButton, _) ->
                Box(
                    Modifier
                        .background(Color.LightGray)
                        .height(64.dp)
                        .width(widthExcludePlayButton)
                ) {
                    Text("Content")
                }
            }
        )
    }
}

/**
 * Contains default values for [PlayableHeader] styling.
 */
object PlayableHeaderDefaults {

    /** Default size for play button */
    val PlayButtonSize: Dp = 128.dp

    /** Default spacing between text and button (8dp) */
    val ButtonSpacing: Dp = 8.dp

    /** Minimum play button size */
    val MinButtonSize: Dp = 56.dp

    /** Horizontal margins around header */
    val Margin: PaddingValues = PaddingValues(horizontal = 16.dp)

    /** Default window insets handling */
    val windowInsets: WindowInsets
        @Composable get() = WindowInsets.systemBars.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Top
        )
}