package com.music.dzr.core.ui

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.theme.DzrTheme

@Composable
fun PlaylistCard(
    name: String,
    pictureUrl: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentDescription = stringResource(R.string.core_ui_cd_playlist)
    MediaGridItem(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .clip(ShapeDefaults.Small)
            .semantics { this.contentDescription = contentDescription },
        headlineContent = {
            Text(
                text = name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        image = {
            TrackListCover(
                coverUrl = pictureUrl,
                contentDescription = null,
                modifier = Modifier.sizeIn(minWidth = 128.dp)
            )
        },
        onClick = onClick,
        onLongClick = onLongClick,
        onLongClickLabel = stringResource(R.string.core_ui_cd_show_more)
    )
}

@Preview
@Composable
private fun PlaylistCardPreview() {
    DzrTheme {
        PlaylistCard(
            name = "2000s Metal",
            pictureUrl = "",
            onClick = {},
            onLongClick = {}
        )
    }
}
