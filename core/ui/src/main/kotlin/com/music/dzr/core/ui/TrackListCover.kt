package com.music.dzr.core.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.designsystem.R as designSystemR

@Composable
fun TrackListCover(
    coverUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    placeholder: Painter = painterResource(designSystemR.drawable.core_design_system_ic_placeholder_default)
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
            modifier = Modifier.size(128.dp)
        )
    }
}