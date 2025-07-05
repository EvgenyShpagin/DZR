package com.music.dzr.core.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage

@Composable
fun InspectableAsyncImage(
    model: Any,
    contentDescription: String?,
    contentScale: ContentScale,
    placeholder: Painter,
    modifier: Modifier = Modifier,
    inspectionImage: Painter? = null
) {
    if (LocalInspectionMode.current) {
        Image(
            painter = inspectionImage ?: painterResource(R.drawable.core_design_system_inspection_placeholder),
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            placeholder = placeholder,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}