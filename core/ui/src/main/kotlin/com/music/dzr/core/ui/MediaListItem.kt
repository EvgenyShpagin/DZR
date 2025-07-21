package com.music.dzr.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.theme.DzrTheme

/**
 * A composable that displays a media item as a row, designed for use in lists like `LazyColumn`.
 *
 * This component acts as a specialized wrapper around the standard Material `ListItem`,
 * providing a consistent API for media content across the application.
 *
 * @param headlineContent The primary content of the list item, typically the title of the media.
 * @param modifier The [Modifier] to be applied to this component.
 * @param supportingContent The secondary content, displayed below the headline. Can be null.
 * @param image The leading content, typically an album cover or artist image. Can be null.
 * @param icon The trailing content, typically an icon for actions like 'more options'. Can be null.
 * @param onClick A lambda to be invoked when the item is clicked.
 * @param onLongClick A lambda to be invoked when the item is long-clicked.
 * @param onLongClickLabel The label for the long click action, used for accessibility.
 * @param enabled Whether the item is enabled and can be interacted with.
 * @param colors The colors to be used for this list item. See [MediaListItemColors].
 * @param disabledImageAlpha The alpha value to apply to the image when the item is disabled.
 */
@Composable
fun MediaListItem(
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: (@Composable () -> Unit)? = null,
    image: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onLongClickLabel: String? = null,
    enabled: Boolean = true,
    colors: MediaListItemColors = MediaItemDefaults.listItemColors(),
    disabledImageAlpha: Float = MediaItemDefaults.DISABLED_IMAGE_ALPHA
) {
    ListItem(
        colors = ListItemDefaults.colors(  // ListItem now uses only enabled colors
            containerColor = Color.Transparent,
            headlineColor = colors.headlineColor(enabled = enabled),
            supportingColor = colors.supportingColor(enabled = enabled),
            trailingIconColor = colors.iconColor(enabled = enabled)
        ),
        leadingContent = if (enabled) image else image?.let {
            {
                Box(modifier = Modifier.alpha(disabledImageAlpha)) {
                    image()
                }
            }
        },
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = icon,
        modifier = modifier.combinedClickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick,
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel
        )
    )
}

/**
 * Represents the container and content colors used in a list item in different states.
 *
 * @param headlineColor the headline text content color of this list item when enabled.
 * @param supportingTextColor the supporting text color of this list item
 * @param iconColor the color of this list item's trailing content when enabled.
 * @param disabledHeadlineColor the content color of this list item when not enabled.
 * @param disabledSupportingTextColor the supporting text color of this list item when not enabled.
 * @param disabledIconColor the color of this list item's trailing content when not enabled.
 * @constructor create an instance with arbitrary colors.
 * See [MediaItemDefaults.listItemColors] for the default colors used in a [MediaListItem].
 */
@Immutable
class MediaListItemColors(
    val headlineColor: Color,
    val supportingTextColor: Color,
    val iconColor: Color,
    val disabledHeadlineColor: Color,
    val disabledSupportingTextColor: Color,
    val disabledIconColor: Color
) {
    /** The color of this [MediaListItem]'s headline text based on enabled state */
    internal fun headlineColor(enabled: Boolean): Color =
        if (enabled) headlineColor else disabledHeadlineColor

    /** The color of this [MediaListItem]'s supporting text based on enabled state */
    internal fun supportingColor(enabled: Boolean): Color =
        if (enabled) supportingTextColor else disabledSupportingTextColor

    /** The color of this [MediaListItem]'s trailing content based on enabled state */
    internal fun iconColor(enabled: Boolean): Color =
        if (enabled) iconColor else disabledIconColor
}

@Preview(name = "Enabled - Full")
@Composable
private fun MediaListItemPreview_EnabledFull() {
    DzrTheme {
        MediaListItem(
            headlineContent = { Text("The Greatest Song Ever Written In The World") },
            supportingContent = { Text("By The Talented Artist & Another One") },
            image = {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Album Art",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentScale = ContentScale.Crop
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More options"
                )
            },
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(name = "Disabled - Full")
@Composable
private fun MediaListItemPreview_DisabledFull() {
    DzrTheme {
        MediaListItem(
            headlineContent = { Text("The Greatest Song Ever Written In The World (Disabled)") },
            supportingContent = { Text("By The Talented Artist & Another One") },
            image = {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Album Art",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentScale = ContentScale.Crop
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More options"
                )
            },
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(name = "Enabled - No Image")
@Composable
private fun MediaListItemPreview_EnabledNoImage() {
    DzrTheme {
        MediaListItem(
            headlineContent = { Text("Song Without Cover") },
            supportingContent = { Text("Anonymous Artist") },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite"
                )
            },
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(name = "Enabled - No Supporting Text")
@Composable
private fun MediaListItemPreview_EnabledNoSupporting() {
    DzrTheme {
        MediaListItem(
            headlineContent = { Text("Headline Only Track") },
            image = {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_slideshow),
                    contentDescription = "Album Art",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentScale = ContentScale.Crop
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More options"
                )
            },
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(name = "Enabled - Minimal")
@Composable
private fun MediaListItem_EnabledMinimal_Preview() {
    DzrTheme {
        MediaListItem(
            headlineContent = { Text("Minimal Item") },
            enabled = true,
            onClick = {}
        )
    }
}
