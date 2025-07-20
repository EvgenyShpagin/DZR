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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
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
        val playButtonSizePx = playButtonSize.roundToPx()
        val minButtonSizePx = minButtonSize.roundToPx()
        val buttonSpacingPx = buttonSpacing.roundToPx()

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val titlesPlaceable = subcompose(PlayableTopAppBarSlot.Titles) {
            Column {
                DisplayText(title)
                if (subtitle != null) {
                    Subtitle(subtitle)
                }
            }
        }.single().measure(looseConstraints)

        val shouldDisplayPlayButton =
            (constraints.maxWidth - titlesPlaceable.width - buttonSpacingPx) >=
                    minButtonSizePx + buttonSpacingPx

        val playButtonPlaceable = subcompose(PlayableTopAppBarSlot.PlayButton) {
            if (shouldDisplayPlayButton) {
                PlayButton(
                    isPlaying = isPlaying,
                    onPlayClick = onPlayClick,
                    modifier = Modifier.size(playButtonSize)
                )
            }
        }.singleOrNull()?.measure(
            Constraints.fixed(playButtonSizePx, playButtonSizePx)
        )

        val playButtonWidth = playButtonPlaceable?.width ?: 0
        val widthExcludePlayButtonDp = (constraints.maxWidth - playButtonWidth).toDp()

        val titleBlockHeightDp = titlesPlaceable.height.toDp()
        val playButtonHeightDp = (playButtonPlaceable?.height ?: 0).toDp()
        val innerSpaceHeight = max(0.dp, playButtonHeightDp - titleBlockHeightDp)

        val layoutData = PlayableHeaderLayout(
            innerSpaceHeight = innerSpaceHeight,
            widthExcludePlayButton = widthExcludePlayButtonDp,
            isButtonDisplayed = shouldDisplayPlayButton
        )

        val contentMaxHeight = if (constraints.hasFixedHeight) {
            (constraints.maxHeight - titlesPlaceable.height).coerceAtLeast(0)
        } else {
            Constraints.Infinity
        }
        val mainContentConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxWidth = constraints.maxWidth,
            maxHeight = contentMaxHeight
        )
        val mainContentPlaceable = subcompose(PlayableTopAppBarSlot.MainContent) {
            content(layoutData)
        }.singleOrNull()?.measure(mainContentConstraints)

        val titleBlockHeightPx = titlesPlaceable.height
        val mainContentHeightPx = mainContentPlaceable?.height ?: 0
        val playButtonHeightPx = playButtonPlaceable?.height ?: 0

        val calculatedHeight = max(titleBlockHeightPx + mainContentHeightPx, playButtonHeightPx)

        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight.takeIf { constraints.hasFixedHeight } ?: calculatedHeight
        ) {
            titlesPlaceable.placeRelative(0, 0)
            mainContentPlaceable?.placeRelative(0, titleBlockHeightPx)
            playButtonPlaceable?.let {
                it.placeRelative(constraints.maxWidth - it.width, 0)
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
        color = MaterialTheme.colorScheme.onSurface,
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