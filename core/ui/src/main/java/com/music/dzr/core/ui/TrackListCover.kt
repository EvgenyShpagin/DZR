package com.music.dzr.core.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.designsystem.R as designSystemR

@Composable
fun TrackListCover(
    coverUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    placeholder: Painter = painterResource(designSystemR.drawable.ic_placeholder_default)
) {
    InspectableAsyncImage(
        model = coverUrl,
        contentDescription = contentDescription,
        placeholder = placeholder,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f)
            .clip(ShapeDefaults.Small)
    )
}

@Preview
@Composable
fun TrackListCoverPreview() {
    DzrTheme {
        TrackListCover(
            coverUrl = "",
            contentDescription = ""
        )
    }
}