package com.music.dzr.core.ui

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.component.ImagePlaceholder
import com.music.dzr.core.designsystem.theme.DzrTheme

@Composable
fun ArtistCard(
    name: String,
    pictureUrl: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentDescription = stringResource(R.string.core_ui_cd_artist, name)
    MediaGridItem(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .semantics { this.contentDescription = contentDescription },
        headlineContent = {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        image = {
            InspectableAsyncImage(
                model = pictureUrl,
                contentDescription = null,
                placeholder = ImagePlaceholder,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .sizeIn(minWidth = 128.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )
        },
        onClick = onClick,
        onLongClick = onLongClick,
        onLongClickLabel = stringResource(R.string.core_ui_cd_show_more)
    )
}

@Preview
@Composable
private fun ArtistCardPreview() {
    DzrTheme {
        ArtistCard(
            name = "The Rolling Stones",
            pictureUrl = "",
            onClick = {},
            onLongClick = {}
        )
    }
}