package com.music.dzr.core.designsystem.theme

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

/**
 * Spacing tokens for the Dzr design system.
 */
@Immutable
data class Dimensions(
    /**
     * The primary screen rhythm applied to screen margins,
     * gutters between elements, and vertical spacing between blocks.
     */
    val spacing: Dp,
    /**
     * The spacing used to visually separate
     * logically distinct blocks within a single screen.
     */
    val sectionSpacing: Dp,
    /**
     * The max width for text-heavy screens such as auth forms,
     * settings, and other reading-oriented content.
     */
    val maxWidthNarrow: Dp,
    /**
     * The max width for standard content screens such as
     * feeds, lists, and detail screens.
     */
    val maxWidthStandard: Dp,
    /**
     * The max width for multi-pane scaffolds such as
     * list-detail and supporting-pane layouts.
     */
    val maxWidthMultiPane: Dp,
)

@VisibleForTesting
internal val CompactDimensions = Dimensions(
    spacing = 16.dp,
    sectionSpacing = 32.dp,
    maxWidthNarrow = Dp.Unspecified,
    maxWidthStandard = Dp.Unspecified,
    maxWidthMultiPane = Dp.Unspecified,
)

@VisibleForTesting
internal val LargeDimensions = Dimensions(
    spacing = 24.dp,
    sectionSpacing = 48.dp,
    maxWidthNarrow = 600.dp,
    maxWidthStandard = 1040.dp,
    maxWidthMultiPane = 1400.dp,
)

internal fun WindowSizeClass.resolveDimensions(): Dimensions {
    // Compact applies to all phone-like experiences, including landscape.
    // Large activates only when both axes are at least Medium.
    val isCompactDevice = !isAtLeastBreakpoint(
        WIDTH_DP_MEDIUM_LOWER_BOUND,
        HEIGHT_DP_MEDIUM_LOWER_BOUND,
    )
    return if (isCompactDevice) CompactDimensions else LargeDimensions
}

internal val LocalDimensions = staticCompositionLocalOf<Dimensions> {
    error("CompositionLocal LocalDimensions not present")
}
