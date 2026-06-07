package com.music.dzr.feature.auth.ui.screen.welcome

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.util.Consumer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.music.dzr.core.designsystem.component.centeredContent
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.mvi.consumeWithLifecycle

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel,
    windowSizeClass: WindowSizeClass,
    onLogin: (String) -> Unit,
    onRestrictAccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val layout = remember(windowSizeClass) { WelcomeLayout.from(windowSizeClass) }

    HandleNewIntents { url ->
        viewModel.onEvent(WelcomeUiEvent.ReturnedFromBrowser(url))
    }

    viewModel.uiEffect.consumeWithLifecycle { effect ->
        when (effect) {
            is WelcomeUiEffect.LoginRequested -> onLogin(effect.authUrl)
            WelcomeUiEffect.RestrictAccessRequested -> onRestrictAccess()
        }
    }

    WelcomeScreen(
        coverMarqueeItems = uiState.coverMarqueeItems,
        layout = layout,
        isLoading = uiState.isLoading,
        onLoginClick = { viewModel.onEvent(WelcomeUiEvent.LoginClicked) },
        onRestrictAccessClick = { viewModel.onEvent(WelcomeUiEvent.RestrictAccessClicked) },
        modifier = modifier.centeredContent(800.dp)
//        modifier = modifier.centeredContent(DzrTheme.dimensions.maxWidthStandard)
    )
}

@Composable
private fun HandleNewIntents(action: (String) -> Unit) {
    val activity = LocalActivity.current as ComponentActivity
    DisposableEffect(activity) {
        val listener = Consumer<Intent> { intent ->
            intent.data?.toString()?.let(action)
        }
        activity.addOnNewIntentListener(listener)
        activity.intent?.let(listener::accept)
        onDispose { activity.removeOnNewIntentListener(listener) }
    }
}

@Composable
@VisibleForTesting
fun WelcomeScreen(
    coverMarqueeItems: List<WelcomeCoverMarqueeItemUiState>,
    layout: WelcomeLayout,
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onRestrictAccessClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = DzrTheme.dimensions.spacing
    val sectionSpacing = DzrTheme.dimensions.sectionSpacing
    val headerPaddings = PaddingValues(
        start = spacing,
        top = spacing,
        end = spacing
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WelcomeHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(headerPaddings)
                .consumeWindowInsets(headerPaddings)
        )
        Spacer(modifier = Modifier.height(32.dp))
        WelcomeCoverMarquee(
            items = coverMarqueeItems,
            rowCount = layout.marqueeRowCount,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(32.dp))
        WelcomeActionButtons(
            isVerticalLayout = layout.buttonsVertical,
            maxVerticalButtonWidth = layout.maxVerticalButtonWidth,
            onLoginClick = onLoginClick,
            onRestrictAccessClick = onRestrictAccessClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing)
        )
        Spacer(modifier = Modifier.weight(1f))
        WelcomeSpotifyAttribution(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(
                    top = 16.dp,
                    bottom = 32.dp
                )
        )
    }
}

@Composable
private fun WelcomeActionButtons(
    isVerticalLayout: Boolean,
    maxVerticalButtonWidth: Dp,
    onLoginClick: () -> Unit,
    onRestrictAccessClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVerticalLayout) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WelcomeLoginButton(
                onClick = onLoginClick,
                modifier = Modifier
                    .widthIn(max = maxVerticalButtonWidth)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            WelcomeRestrictAccessButton(
                onClick = onRestrictAccessClick,
                modifier = Modifier
                    .widthIn(max = maxVerticalButtonWidth)
                    .fillMaxWidth()
            )
        }
    } else {
        Row(modifier = modifier) {
            WelcomeRestrictAccessButton(
                onClick = onRestrictAccessClick,
                modifier = Modifier.height(56.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            WelcomeLoginButton(
                onClick = onLoginClick,
                modifier = Modifier
                    .height(56.dp)
                    .weight(1f)
            )
        }
    }
}

//@Preview(name = "Phone portrait")
//@Preview(
//    name = "Phone landscape",
//    device = "spec:width=${WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND}dp,height=360dp"
//)
@Preview(name = "Tablet", device = TABLET)
@Composable
private fun WelcomeScreenPreview() {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    DzrTheme {
        Surface {
            WelcomeScreen(
                coverMarqueeItems = previewCoverMarquees,
                layout = WelcomeLayout.from(windowSizeClass),
                isLoading = false,
                onLoginClick = {},
                onRestrictAccessClick = {},
                modifier = Modifier
            )
        }
    }
}

@VisibleForTesting
data class WelcomeLayout(
    val marqueeRowCount: Int,
    val buttonsVertical: Boolean,
    val maxVerticalButtonWidth: Dp // TODO: Если будет введен LocalDimensions, то это наверное стоит опустить.
) {
    companion object {
        // TODO: нужно использовать локальные ограничения!!
        // Так как находясь внутри ListDetail Layout ширина будет гораздо меньше!
        fun from(windowSizeClass: WindowSizeClass): WelcomeLayout {
            val isAtLeastMediumHeight = windowSizeClass.isHeightAtLeastMedium()
            val isAtLeastMediumWidth = windowSizeClass.isWidthAtLeastMedium()

            return WelcomeLayout(
                marqueeRowCount = if (isAtLeastMediumHeight) 2 else 1,
                buttonsVertical = isAtLeastMediumHeight,
                maxVerticalButtonWidth = if (isAtLeastMediumWidth) 480.dp else Dp.Infinity
            )
        }
    }
}

fun WindowSizeClass.isHeightAtLeastMedium(): Boolean {
    return isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
}

fun WindowSizeClass.isWidthAtLeastMedium(): Boolean {
    return isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
}
