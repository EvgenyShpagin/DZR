package com.music.dzr.feature.auth.ui.screen.welcome

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color.RGBToHSV
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.size.Size
import coil3.toBitmap
import com.music.dzr.core.designsystem.component.ImagePlaceholder
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.ui.InspectableAsyncImage
import com.music.dzr.feature.auth.R

/**
 * A release cover tile that adds a soft colored glow based on the image's dominant color.
 *
 * The image is displayed via `InspectableAsyncImage`, which shows [placeholder] in Compose Preview
 * (no network/IO), and loads the actual image at runtime.
 *
 * Glow color can be explicitly overridden via [CoverMarqueeItemUiState.dominantColor].
 * If it is [Color.Unspecified], the dominant color is computed from the loaded bitmap and used for the glow.
 *
 * @param item The state object containing image URL and optional color override.
 * @param modifier The [Modifier] to be applied to this item.
 * @param contentDescription Content description for accessibility.
 * @param shape The clipping shape of the image tile. Propagated to the glow shape as well.
 * @param glowRadius The apparent size of the glow shadow.
 * @param isBright If true, uses a brighter glow; otherwise a darker one.
 * @param placeholder Painter shown while loading. Also used as the preview image.
 */
@Composable
internal fun CoverMarqueeItem(
    item: CoverMarqueeItemUiState,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shape: Shape = ShapeDefaults.ExtraSmall,
    glowRadius: Dp = 16.dp,
    isBright: Boolean = !isSystemInDarkTheme(),
    placeholder: Painter = ImagePlaceholder,
) {
    val imageBitmap by rememberBitmapFromUrl(item.imageUrl)
    val resolvedGlowColor = remember(imageBitmap) {
        item.dominantColor.takeOrElse {
            imageBitmap?.dominantColor() ?: Color.Unspecified
        }
    }

    val glowModifier = if (resolvedGlowColor.isSpecified) {
        Modifier.glow(
            color = resolvedGlowColor,
            isBright = isBright,
            radius = glowRadius,
            shape = shape
        )
    } else {
        Modifier
    }

    InspectableAsyncImage(
        model = imageBitmap,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        placeholder = placeholder,
        modifier = modifier
            .then(glowModifier)
            .aspectRatio(1f)
            .clip(shape)
    )
}

/**
 * Loads a [Bitmap] for [imageUrl] and exposes it as state.
 * Cancels and restarts automatically when keys change.
 */
@Composable
private fun rememberBitmapFromUrl(
    imageUrl: String,
    size: Size = Size.ORIGINAL
): State<Bitmap?> {
    val context = LocalContext.current
    val imageLoader = SingletonImageLoader.get(context.applicationContext)
    return produceState(initialValue = null, imageUrl, size, context) {
        value = convertImageUrlToBitmap(
            imageUrl = imageUrl,
            context = context,
            size = size,
            imageLoader = imageLoader
        )
    }
}

/**
 * Load a Bitmap from the given [imageUrl] using Coil 3.
 *
 * - Reuses the shared ImageLoader via [SingletonImageLoader] (no per-call builders).
 * - Disables hardware bitmaps to ensure a software [Bitmap] result for safe access/manipulation.
 * - Returns null on failure (network error, decoding error, etc.).
 */
private suspend fun convertImageUrlToBitmap(
    imageUrl: String,
    context: Context,
    size: Size = Size.ORIGINAL,
    imageLoader: ImageLoader = SingletonImageLoader.get(context.applicationContext)
): Bitmap? {
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .size(size)
        .allowHardware(false)
        .build()

    val result = imageLoader.execute(request)
    return if (result is SuccessResult) result.image.toBitmap() else null
}

private fun Bitmap.dominantColor(): Color {
    val palette = Palette.from(this)
        .maximumColorCount(10)
        .generate()
    val swatch = palette.dominantSwatch
        ?: palette.vibrantSwatch
        ?: palette.mutedSwatch
    return if (swatch != null) Color(swatch.rgb) else Color.Unspecified
}

/**
 * Applies a soft colored drop shadow around the composable to simulate a glow.
 *
 * The hue of [color] is preserved while saturation and lightness are tuned to a pleasing
 * ambient value.
 *
 * @param color Base color to derive the glow from. If [Color.Unspecified], no-op.
 * @param isBright If true, uses a lighter glow; otherwise a darker one.
 * @param radius Apparent size of the glow.
 * @param shape Shape of the shadow blur (should match the clipped content for best results).
 */
private fun Modifier.glow(
    color: Color,
    isBright: Boolean = true,
    radius: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(8.dp)
): Modifier {
    if (color.isUnspecified) return this

    val hue = color.hsvHue()
    val glowColor = Color.hsl(
        hue = hue,
        saturation = 0.5f,
        lightness = if (isBright) 0.75f else 0.25f,
    )
    return dropShadow(
        shape = shape,
        shadow = Shadow(
            radius = radius,
            spread = 4.dp,
            color = glowColor,
        )
    )
}

/**
 * Extract the HSV hue component of this [Color].
 */
private fun Color.hsvHue(): Float {
    val hsv = FloatArray(3)
    RGBToHSV(
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt(),
        hsv
    )
    return hsv[0]
}


@Preview(name = "Light Mode, 128dp")
@Composable
private fun CoverMarqueeItemPreviewLight128dp() {
    DzrTheme {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            CoverMarqueeItem(
                item = CoverMarqueeItemUiState(
                    imageUrl = "",
                    dominantColor = previewImageDominantColor
                ),
                placeholder = previewImagePainter,
                modifier = Modifier.size(128.dp)
            )
        }
    }
}

@Preview(
    name = "Dark Mode, 96dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun CoverMarqueeItemPreviewDark96dp() {
    DzrTheme {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            CoverMarqueeItem(
                item = CoverMarqueeItemUiState(
                    imageUrl = "",
                    dominantColor = previewImageDominantColor
                ),
                placeholder = previewImagePainter,
                modifier = Modifier.size(96.dp)
            )
        }
    }
}

@Preview(name = "Light Mode, 64dp, Extra Glow")
@Composable
private fun CoverMarqueeItemPreviewLight64dpBigRadius() {
    DzrTheme {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(32.dp)
        ) {
            CoverMarqueeItem(
                item = CoverMarqueeItemUiState(
                    imageUrl = "",
                    dominantColor = previewImageDominantColor
                ),
                glowRadius = 32.dp,
                placeholder = previewImagePainter,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

private val previewImageDominantColor
    @Composable get() = ContextCompat.getDrawable(
        LocalContext.current,
        R.drawable.feature_auth_preview_release_cover
    )!!.toBitmap().dominantColor()

private val previewImagePainter
    @Composable get() = painterResource(R.drawable.feature_auth_preview_release_cover)
