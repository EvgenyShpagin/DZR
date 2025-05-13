package com.music.dzr.core.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.music.dzr.core.designsystem.theme.DzrTheme

@Composable
fun RowScope.DzrNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel
    )
}

@Composable
fun DzrNavigationBar(
    modifier: Modifier = Modifier,
    shape: Shape = DzrNavigationDefaults.Shape,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        content = content,
        modifier = modifier
            .windowInsetsPadding(NavigationBarDefaults.windowInsets)
            .clip(shape)
    )
}

@Preview
@Composable
private fun DzrNavigationBarPreview() {
    DzrTheme {
        DzrNavigationBar {
            repeat(3) { i ->
                DzrNavigationBarItem(
                    selected = i == 0,
                    onClick = {},
                    label = {
                        Text("Item $i")
                    },
                    icon = {
                        Icon(Icons.Default.Favorite, null)
                    }
                )
            }
        }
    }
}

@Composable
fun DzrNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel
    )
}

@Composable
fun DzrNavigationRail(
    modifier: Modifier = Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    NavigationRail(
        modifier = modifier,
        containerColor = DzrNavigationDefaults.RailContainerColor,
        header = header,
        content = content
    )
}

@Preview
@Composable
private fun DzrNavigationRailPreview() {
    DzrTheme {
        DzrNavigationRail {
            repeat(3) { i ->
                DzrNavigationRailItem(
                    selected = i == 0,
                    onClick = {},
                    label = {
                        Text("Item $i")
                    },
                    icon = {
                        Icon(Icons.Default.Favorite, null)
                    }
                )
            }
        }
    }
}

@Composable
fun DzrNavigationSuiteScaffold(
    navigationSuiteItems: DzrNavigationSuiteScope.() -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    content: @Composable () -> Unit,
) {
    val layoutType = NavigationSuiteScaffoldDefaults
        .calculateFromAdaptiveInfo(windowAdaptiveInfo)

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            DzrNavigationSuiteScope(navigationSuiteScope = this)
                .run(navigationSuiteItems)
        },
        layoutType = layoutType,
        containerColor = Color.Transparent,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationRailContainerColor = DzrNavigationDefaults.RailContainerColor,
        ),
        modifier = modifier,
    ) {
        content()
    }
}

class DzrNavigationSuiteScope internal constructor(
    private val navigationSuiteScope: NavigationSuiteScope,
) {
    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        icon: @Composable () -> Unit,
        selectedIcon: @Composable () -> Unit = icon,
        label: @Composable (() -> Unit)? = null,
    ) = navigationSuiteScope.item(
        selected = selected,
        onClick = onClick,
        icon = {
            if (selected) {
                selectedIcon()
            } else {
                icon()
            }
        },
        label = label,
        modifier = modifier,
    )
}


object DzrNavigationDefaults {
    val Shape = ShapeDefaults.Large
    val RailContainerColor = Color.Transparent
}