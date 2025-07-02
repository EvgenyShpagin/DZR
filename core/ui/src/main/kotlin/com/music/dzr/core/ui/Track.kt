package com.music.dzr.core.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.designsystem.R as designSystemR

@Composable
fun Track(
    coverUrl: String?,
    onClick: () -> Unit,
    title: String,
    explicit: Boolean,
    contributors: List<String>,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = onMoreClick
) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        leadingContent = coverUrl?.let { url ->
            {
                InspectableAsyncImage(
                    model = url,
                    contentDescription = stringResource(R.string.core_ui_cd_track_cover),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(designSystemR.drawable.ic_placeholder_default),
                    modifier = Modifier.clip(ShapeDefaults.ExtraSmall)
                )
            }
        },
        headlineContent = {
            Text(
                text = if (explicit) "$title \uD83C\uDD74" else title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        supportingContent = {
            Text(
                text = formatContributors(contributors),
                maxLines = 1
            )
        },
        trailingContent = {
            IconButton(
                onClick = onMoreClick,
                modifier = Modifier.offset(x = 8.dp)
            ) {
                Icon(
                    DzrIcons.MoreVert,
                    contentDescription = stringResource(R.string.core_ui_cd_show_more)
                )
            }
        },
        modifier = modifier.combinedClickable(
            role = Role.Button,
            onClick = onClick,
            onLongClick = onLongClick,
            onLongClickLabel = stringResource(R.string.core_ui_cd_show_more)
        )
    )
}

@Preview
@Composable
private fun TrackPreview() {
    DzrTheme {
        Track(
            coverUrl = "",
            title = "Sample Track",
            explicit = true,
            onClick = {},
            onMoreClick = {},
            contributors = listOf("Artist 1", "Artist 2")
        )
    }
}

@Preview
@Composable
private fun TrackWithoutCoverPreview() {
    DzrTheme {
        Track(
            coverUrl = null,
            title = "Sample Track",
            explicit = true,
            onClick = {},
            onMoreClick = {},
            contributors = listOf("Artist 1", "Artist 2")
        )
    }
}
