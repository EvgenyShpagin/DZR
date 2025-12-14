package com.music.dzr.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.theme.DzrTheme

/**
 * A composable that displays a media item as a tile, designed for use in carousels (`LazyRow`)
 * or grids (`LazyVerticalGrid`).
 *
 * This component arranges content vertically, with a mandatory image at the top.
 *
 * @param headlineContent The primary text content, displayed below the image.
 * @param image The main visual content, typically the album cover or artist image.
 * @param modifier The [Modifier] to be applied to this component.
 * @param supportingContent The secondary text content, displayed below the headline. Can be null.
 * @param onClick A lambda to be invoked when the item is clicked.
 * @param onLongClick A lambda to be invoked when the item is long-clicked.
 */
@Composable
fun MediaGridItem(
    headlineContent: @Composable () -> Unit,
    image: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onLongClickLabel: String? = null,
    enabled: Boolean = true,
    colors: MediaGridItemColors = MediaItemDefaults.gridItemColors(),
    disabledImageAlpha: Float = MediaItemDefaults.DISABLED_IMAGE_ALPHA
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.combinedClickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick,
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel
        )
    ) {
        Box(
            modifier = Modifier.alpha(
                if (enabled) 1f else disabledImageAlpha
            )
        ) {
            image()
        }
        Spacer(modifier = Modifier.height(4.dp))
        ProvideTextStyle(
            value = MaterialTheme.typography.titleSmall
                .copy(color = colors.headlineColor(enabled = enabled))
        ) {
            headlineContent()
        }
        supportingContent?.let { supportingContent ->
            ProvideTextStyle(
                value = MaterialTheme.typography.labelSmall
                    .copy(color = colors.supportingColor(enabled = enabled))
            ) {
                supportingContent()
            }
        }
    }
}

/**
 * Represents the container and content colors used in a list item in different states.
 *
 * @param headlineColor the headline text content color of this list item when enabled.
 * @param supportingTextColor the supporting text color of this list item
 * @param disabledSupportingTextColor the supporting text color of this list item when not enabled.
 * @param disabledHeadlineColor the content color of this list item when not enabled.
 * @constructor create an instance with arbitrary colors.
 * See [MediaItemDefaults.gridItemColors] for the default colors used in a [MediaGridItem].
 */
@Immutable
class MediaGridItemColors(
    val headlineColor: Color,
    val supportingTextColor: Color,
    val disabledSupportingTextColor: Color,
    val disabledHeadlineColor: Color
) {
    /** The color of this [MediaGridItem]'s headline text based on enabled state */
    internal fun headlineColor(enabled: Boolean): Color =
        if (enabled) headlineColor else disabledHeadlineColor

    /** The color of this [MediaGridItem]'s supporting text based on enabled state */
    internal fun supportingColor(enabled: Boolean): Color =
        if (enabled) supportingTextColor else disabledSupportingTextColor
}

@PreviewLightDark
@Composable
private fun MediaGridItemPreview() {
    DzrTheme {
        Surface {
            MediaGridItem(
                headlineContent = {
                    Text(
                        "Awesome Album",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                image = {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentScale = ContentScale.Crop
                    )
                },
                supportingContent = {
                    Text(
                        "The Best Artist",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                enabled = true,
                onClick = {}
            )
        }
    }
}
