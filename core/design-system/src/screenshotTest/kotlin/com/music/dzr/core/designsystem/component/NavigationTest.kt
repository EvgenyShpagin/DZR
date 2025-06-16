package com.music.dzr.core.designsystem.component

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrNavigationBar_Preview() {
    ExampleDzrNavigationBar()
}

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrNavigationRail_Preview() {
    ExampleDzrNavigationRail()
}

@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun DzrNavigationBar_FontScale_1_5_Preview() {
    ExampleDzrNavigationBar()
}

@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun DzrNavigationRail_FontScale_1_5_Preview() {
    ExampleDzrNavigationRail()
}


@Composable
private fun ExampleDzrNavigationBar() {
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
                        Icon(DzrIcons.Favorite, null)
                    }
                )
            }
        }
    }
}

@Composable
private fun ExampleDzrNavigationRail() {
    DzrTheme {
        DzrNavigationRail(Modifier.height(300.dp)) {
            repeat(3) { i ->
                DzrNavigationRailItem(
                    selected = i == 0,
                    onClick = {},
                    label = {
                        Text("Item $i")
                    },
                    icon = {
                        Icon(DzrIcons.Favorite, null)
                    }
                )
            }
        }
    }
}