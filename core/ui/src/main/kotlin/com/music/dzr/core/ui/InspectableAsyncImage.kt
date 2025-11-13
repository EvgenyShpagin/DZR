package com.music.dzr.core.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import coil3.compose.AsyncImage

/**
 * A lightweight wrapper around Coil's [AsyncImage] that behaves well in Compose Preview.
 *
 * When running inside a preview (i.e. [LocalInspectionMode] is true), this composable renders
 * the provided [placeholder] using a static [Image] and skips any network/IO work. At runtime,
 * it delegates to [AsyncImage] with the same parameters.
 *
 * @param model The image model accepted by Coil (URL/String/Uri/Bitmap/etc).
 * @param contentDescription Accessibility label for the image.
 * @param contentScale How to scale the image inside its bounds.
 * @param placeholder Painter shown while the image is loading. This painter is also displayed
 * in Compose Preview.
 * @param modifier Optional [Modifier] for this image.
 */
@Composable
fun InspectableAsyncImage(
    model: Any?,
    contentDescription: String?,
    contentScale: ContentScale,
    placeholder: Painter,
    modifier: Modifier = Modifier,
) {
    if (LocalInspectionMode.current) {
        Image(
            painter = placeholder,
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