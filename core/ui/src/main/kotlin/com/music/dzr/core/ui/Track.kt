package com.music.dzr.core.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
    state: TrackUiState,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = onMoreClick
) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        leadingContent = state.coverUrl?.let { url ->
            {
                InspectableAsyncImage(
                    model = url,
                    contentDescription = stringResource(R.string.core_ui_cd_track_cover),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(designSystemR.drawable.core_design_system_ic_placeholder_default),
                    modifier = Modifier.clip(ShapeDefaults.ExtraSmall)
                        .size(56.dp)
                )
            }
        },
        headlineContent = {
            Text(
                text = if (state.isExplicit) "${state.title} \uD83C\uDD74" else state.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        supportingContent = {
            Text(
                text = formatContributors(state.contributors),
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
    val uiState = TrackUiState(
        coverUrl = "",
        title = "Sample Track",
        isExplicit = true,
        contributors = listOf("Artist 1", "Artist 2")
    )
    DzrTheme {
        Track(
            state = uiState,
            onClick = {},
            onMoreClick = {},
        )
    }
}

@Preview
@Composable
private fun TrackWithoutCoverPreview() {
    val uiState = TrackUiState(
        coverUrl = "",
        title = "Sample Track",
        isExplicit = true,
        contributors = listOf("Artist 1", "Artist 2")
    )
    DzrTheme {
        Track(
            state = uiState,
            onClick = {},
            onMoreClick = {},
        )
    }
}
