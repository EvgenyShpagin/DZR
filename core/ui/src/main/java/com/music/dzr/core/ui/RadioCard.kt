package com.music.dzr.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.designsystem.R as designSystemR

@Composable
fun RadioCard(
    name: String,
    pictureUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(128.dp)
            .aspectRatio(1f)
            .clip(ShapeDefaults.Small)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        InspectableAsyncImage(
            model = pictureUrl,
            contentDescription = stringResource(R.string.cd_radio),
            placeholder = painterResource(designSystemR.drawable.ic_placeholder_default),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            name,
            style = MaterialTheme.typography.labelLarge.copy(
                shadow = radioNameShadow()
            ),
            maxLines = 2,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun radioNameShadow(): Shadow {
    val density = LocalDensity.current
    val blurRadius = with(density) { 1.dp.toPx() }
    val offsetPx = with(density) { 0.5.dp.toPx() }
    val offset = Offset(offsetPx, offsetPx)
    return Shadow(offset = offset, blurRadius = blurRadius)
}

@Preview
@Composable
private fun RadioCardPreview() {
    DzrTheme {
        RadioCard(
            name = "Summer Afternoon",
            pictureUrl = "",
            onClick = {}
        )
    }
}