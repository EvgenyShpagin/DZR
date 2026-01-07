package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.ui.PlayableHeaderDefaults
import com.music.dzr.feature.auth.R
import kotlin.math.roundToInt

@Composable
internal fun WelcomeHeader(
    modifier: Modifier = Modifier,
    appName: String = getAppName(),
    windowInsets: WindowInsets = PlayableHeaderDefaults.windowInsets
) {
    val displayLarge = MaterialTheme.typography.displayLarge
    val labelLarge = MaterialTheme.typography.labelLarge

    val preposition = stringResource(R.string.feature_auth_welcome_title_preposition)
    val prepositionHeight = rememberTextHeight(labelLarge, preposition)
    val appNameHeight = rememberTextHeight(displayLarge, appName)

    val titleText = @Composable {
        Text(
            text = stringResource(R.string.feature_auth_welcome_title),
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
            style = displayLarge
        )
    }
    val prepositionText = @Composable {
        if (preposition.isNotBlank()) {
            Text(
                text = preposition,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = labelLarge
            )
        }
    }
    val appNameText = @Composable {
        Text(
            text = appName,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = displayLarge
        )
    }

    WelcomeHeaderLayout(
        modifier = modifier.windowInsetsPadding(windowInsets),
        titleText = titleText,
        prepositionText = prepositionText,
        appNameText = appNameText,
        prepositionHeight = prepositionHeight,
        appNameHeight = appNameHeight
    )
}

@Composable
private fun WelcomeHeaderLayout(
    modifier: Modifier,
    titleText: @Composable () -> Unit,
    prepositionText: @Composable () -> Unit,
    appNameText: @Composable () -> Unit,
    prepositionHeight: Float,
    appNameHeight: Float
) {
    Layout(
        contents = listOf(titleText, prepositionText, appNameText),
        modifier = modifier
    ) { (titleMeasurables, prepositionMeasurables, appNameMeasurables), constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val titlePlaceable = titleMeasurables.first().measure(looseConstraints)
        val titleBaseline = titlePlaceable[LastBaseline]

        val prepositionPlaceable = prepositionMeasurables.firstOrNull()?.measure(looseConstraints)
        val prepositionLastBaseline = prepositionPlaceable?.get(LastBaseline) ?: 0
        val prepositionFirstBaseline = prepositionPlaceable?.get(FirstBaseline) ?: 0

        val appNamePlaceable = appNameMeasurables.first().measure(looseConstraints)
        val appNameFirstBaseline = appNamePlaceable[FirstBaseline]

        val spacing = 16.dp.roundToPx()

        val targetPrepositionBaseline = titleBaseline + spacing + prepositionHeight

        val prepositionY = (targetPrepositionBaseline - prepositionFirstBaseline).roundToInt()

        val actualPrepositionBaseline = prepositionY + prepositionLastBaseline

        val targetAppNameBaseline = if (prepositionPlaceable != null) {
            actualPrepositionBaseline + spacing + appNameHeight
        } else {
            titleBaseline + spacing + appNameHeight
        }

        val appNameY = (targetAppNameBaseline - appNameFirstBaseline).roundToInt()

        val totalWidth = maxOf(
            titlePlaceable.width,
            prepositionPlaceable?.width ?: 0,
            appNamePlaceable.width
        )
        val totalHeight = appNameY + appNamePlaceable.height

        layout(
            width = constraints.constrainWidth(totalWidth),
            height = constraints.constrainHeight(totalHeight)
        ) {
            titlePlaceable.place(0, 0)
            prepositionPlaceable?.place(0, prepositionY)
            appNamePlaceable.place(0, appNameY)
        }
    }
}

@Composable
private fun getAppName(): String {
    return with(LocalContext.current) {
        packageManager.getApplicationLabel(applicationInfo).toString()
    }
}

@Composable
private fun rememberTextHeight(style: TextStyle, text: String): Float {
    if (text.isEmpty()) return 0f

    val density = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val resolvedTypeface = remember(style) {
        fontFamilyResolver.resolve(style.fontFamily, style.fontWeight ?: FontWeight.Normal)
    }

    return remember(resolvedTypeface, density, style, text) {
        val paint = android.graphics.Paint()
        paint.typeface = resolvedTypeface.value as android.graphics.Typeface
        paint.textSize = with(density) { style.fontSize.toPx() }
        val rect = android.graphics.Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        // rect.top is negative (distance up from baseline).
        -rect.top.toFloat()
    }
}

@PreviewFontScale
@Composable
private fun WelcomeHeaderPreview() {
    DzrTheme {
        Surface {
            WelcomeHeader(appName = "Audyra")
        }
    }
}
