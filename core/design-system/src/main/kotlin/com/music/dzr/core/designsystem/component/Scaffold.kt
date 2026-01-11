package com.music.dzr.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Dzr wrapper around [Scaffold] with project-specific defaults.
 *
 * Sets [containerColor] to [Color.Transparent] by default to allow the [DzrBackground]
 * to show through.
 *
 * @param modifier the [Modifier] to be applied to this scaffold
 * @param topBar top app bar of the screen, typically a [DzrTopAppBar]
 * @param bottomBar bottom bar of the screen
 * @param snackbarHost component to host Snackbars that are pushed to be shown
 * @param floatingActionButton Main action button of the screen
 * @param floatingActionButtonPosition position of the FAB on the screen
 * @param containerColor the color used for the background of this scaffold. Use [Color.Transparent]
 * to have no solid background.
 * @param contentColor the preferred color for content inside this scaffold. Defaults to
 * [MaterialTheme.colorScheme.onBackground].
 * @param contentWindowInsets window insets to be passed to content slot via PaddingValues params
 * @param content content of the screen. The lambda receives a [PaddingValues] that should be
 * applied to the content root via [Modifier.padding] and [Modifier.consumeWindowInsets] to
 * properly offset top and bottom bars.
 */
@Composable
fun DzrScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content
    )
}
