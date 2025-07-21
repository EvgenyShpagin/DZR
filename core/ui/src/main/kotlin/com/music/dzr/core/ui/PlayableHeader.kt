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
 * A customizable header component that features play controls, titles, and an area for custom content,
 * all within an adaptive layout.
 *
 * This component uses [SubcomposeLayout] to efficiently measure and arrange its complex content,
 * ensuring optimal performance.
 *
 * @param title The main title of the header.
 * @param onPlayClick The callback to be invoked when the play button is clicked.
 * @param isPlaying A boolean indicating whether the content is currently playing, which determines the play button's icon.
 * @param modifier The [Modifier] to be applied to this component.
 * @param subtitle An optional subtitle to display below the main title.
 * @param playButtonSize The maximum size of the play button. Defaults to [PlayableHeaderDefaults.PlayButtonSize].
 * @param buttonSpacing The spacing between the text content and the play button. Defaults to [PlayableHeaderDefaults.ButtonSpacing].
 * @param horizontalMargin The horizontal padding for the header. Defaults to [PlayableHeaderDefaults.Margin].
 * @param minButtonSize The minimum size of the play button. If the available space is less than this, the button will not be shown. Defaults to [PlayableHeaderDefaults.MinButtonSize].
 * @param windowInsets The window insets to apply to the header. Defaults to [PlayableHeaderDefaults.windowInsets].
 * @param content A slot for custom content to be displayed below the titles. It receives a [PlayableHeaderLayout]
 * object providing layout details such as:
 * - `innerSpaceHeight`: Available vertical space between the titles and the bottom of the header.
 * - `widthExcludePlayButton`: Available width for content, excluding the play button area.
 * - `isButtonDisplayed`: Indicates if the play button is currently being displayed.
 *
 * @see PlayableHeaderLayout Provides layout parameters to the content slot.
 * @see PlayableHeaderDefaults Contains default styling values for the header.
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
 * A private composable that handles the layout logic for [PlayableHeader].
 *
 * It uses [SubcomposeLayout] to measure and place the title, subtitle, play button, and custom content
 * based on the available space.
 *
 * @param title The main title to display.
 * @param subtitle An optional subtitle to display below the title.
 * @param isPlaying A boolean indicating if the content is currently playing.
 * @param onPlayClick A callback invoked when the play button is clicked.
 * @param playButtonSize The maximum size of the play button.
 * @param buttonSpacing The spacing between the text and the play button.
 * @param minButtonSize The minimum size required for the play button to be displayed.
 * @param content The main content slot of the header.
 * @param modifier The modifier to be applied to the layout.
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

        // Measure title to determine its preferred width
        val titlePlaceable = subcompose(PlayableHeaderLayoutContent.Title) {
            PlayableHeaderTitle(title = title)
        }.single().measure(looseConstraints)
        val titleWidth = titlePlaceable.width

        // Determine if play button can be displayed and its potential width
        val potentialButtonWidthPx = (layoutWidth - titleWidth - buttonSpacingPx)
            .coerceAtMost(playButtonSizePx)
        val shouldDisplayPlayButton = potentialButtonWidthPx >= minButtonSizePx

        // Measure subtitle with width constrained by the button area
        val subtitleMaxWidth = layoutWidth - potentialButtonWidthPx - buttonSpacingPx
        val subtitlePlaceable = subcompose(PlayableHeaderLayoutContent.Subtitle) {
            if (subtitle != null) {
                PlayableHeaderSubtitle(subtitle = subtitle)
            }
        }.singleOrNull()?.measure(looseConstraints.copy(maxWidth = subtitleMaxWidth))

        val titlesHeight = titlePlaceable.height + (subtitlePlaceable?.height ?: 0)

        // Measure play button if it should be displayed
        val playButtonPlaceable = subcompose(PlayableHeaderLayoutContent.PlayButton) {
            if (shouldDisplayPlayButton) {
                PlayableHeaderButton(
                    isPlaying = isPlaying,
                    onClick = onPlayClick,
                    modifier = Modifier.size(potentialButtonWidthPx.toDp())
                )
            }
        }.singleOrNull()?.measure(Constraints.fixed(potentialButtonWidthPx, potentialButtonWidthPx))

        val playButtonWidth = playButtonPlaceable?.width ?: 0
        val playButtonHeight = playButtonPlaceable?.height ?: 0

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
            subtitlePlaceable?.placeRelative(0, titlePlaceable.height)

            // Place main content below titles
            if (finalHeight - titlesHeight > 0) {
                contentPlaceables.forEach { it.placeRelative(0, titlesHeight) }
            }

            // Place play button at the end
            playButtonPlaceable?.let { it.placeRelative(layoutWidth - it.width, 0) }
        }
    }
}

/** Displays the main title of the header. */
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

/** Displays the subtitle of the header. */
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

/** Displays the play/pause button, adapting its icon based on the [isPlaying] state. */
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
        val (icon, contentDescriptionRes) = if (isPlaying) {
            DzrIcons.Pause to R.string.core_ui_cd_pause_music
        } else {
            DzrIcons.PlayArrow to R.string.core_ui_cd_play_music
        }

        Icon(
            imageVector = icon,
            contentDescription = stringResource(contentDescriptionRes)
        )
    }
}

/**
 * Provides layout parameters to the content slot of [PlayableHeader].
 *
 * @property innerSpaceHeight The available vertical space for custom content, calculated from the
 * height difference between the play button and the text titles.
 * @property widthExcludePlayButton The available width for the content slot, excluding the play button area.
 * @property isButtonDisplayed A flag indicating whether the play button is currently visible.
 */
@Immutable
data class PlayableHeaderLayout(
    val innerSpaceHeight: Dp,
    val widthExcludePlayButton: Dp,
    val isButtonDisplayed: Boolean
)

/** Contains default values used by the [PlayableHeader] component. */
object PlayableHeaderDefaults {
    /** The default size for the play button. */
    val PlayButtonSize: Dp = 128.dp

    /** The default spacing between the text and the button. */
    val ButtonSpacing: Dp = 8.dp

    /** The default minimum size for the play button. */
    val MinButtonSize: Dp = 56.dp

    /** The default horizontal margins for the header. */
    val Margin: PaddingValues = PaddingValues(horizontal = 16.dp)

    /** The default window insets handled by the header. */
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
            subtitle = "This is a very long subtitle that should be wrapped and constrained by the title width",
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
