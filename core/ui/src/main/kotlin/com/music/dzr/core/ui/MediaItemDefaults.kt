package com.music.dzr.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Contains the default values used by [MediaListItem] and [MediaGridItem]. */
object MediaItemDefaults {

    /** The alpha of the image when it is disabled. */
    const val DISABLED_IMAGE_ALPHA = 0.5f

    /**
     * Creates a [MediaListItemColors] that represents the default container and content colors
     * used in a [MediaListItem] and [MediaGridItem].
     *
     * @param headlineColor the headline text content color of this list item when enabled.
     * @param supportingColor the supporting text color of this list item
     * @param iconColor the color of this list item's icon when enabled.
     * @param disabledHeadlineColor the content color of this list item when not enabled.
     * @param disabledSupportingColor the supporting text color of this list item when not enabled.
     * @param disabledIconColor the color of this list item's icon when not enabled.
     */
    @Composable
    fun listItemColors(
        headlineColor: Color = MaterialTheme.colorScheme.onSurface,
        supportingColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledHeadlineColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        disabledSupportingColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        disabledIconColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    ) = MediaListItemColors(
        headlineColor = headlineColor,
        supportingTextColor = supportingColor,
        iconColor = iconColor,
        disabledHeadlineColor = disabledHeadlineColor,
        disabledSupportingTextColor = disabledSupportingColor,
        disabledIconColor = disabledIconColor
    )
}
