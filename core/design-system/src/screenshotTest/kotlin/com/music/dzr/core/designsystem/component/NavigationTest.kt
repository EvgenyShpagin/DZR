package com.music.dzr.core.designsystem.component

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDarkAndFontScale
@Composable
private fun DzrNavigationBar_Preview() {
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

@PreviewTest
@PreviewLightDarkAndFontScale
@Composable
private fun DzrNavigationRail_Preview() {
    DzrTheme {
        Surface {
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
}
