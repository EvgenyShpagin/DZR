package com.music.dzr.core.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview

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
    onLongClickLabel: String? = null
) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        leadingContent = image,
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = icon,
        modifier = modifier.combinedClickable(
            role = Role.Button,
            onClick = onClick,
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel
        )
    )
}
