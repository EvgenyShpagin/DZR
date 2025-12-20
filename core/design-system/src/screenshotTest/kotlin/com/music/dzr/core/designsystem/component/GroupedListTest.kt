package com.music.dzr.core.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDarkAndFontScale
@Composable
private fun GroupedList_Preview() {
    DzrTheme {
        Surface {
            GroupedList {
                GroupedListItem(
                    leadingText = { Text("Default item") },
                    onClick = {}
                )
                GroupedListItem(
                    leadingText = { Text("With leading icon") },
                    leadingIcon = { Icon(DzrIcons.Favorite, contentDescription = null) },
                    onClick = {}
                )
                GroupedListItem(
                    leadingText = { Text("With trailing text") },
                    trailingText = { Text("Trailing text") },
                    onClick = {}
                )
                GroupedListItem(
                    leadingText = { Text("With trailing icon") },
                    trailingIcon = { Icon(DzrIcons.MoreVert, contentDescription = null) },
                    onClick = {}
                )
                GroupedListItem(
                    leadingText = { Text("Disabled") },
                    leadingIcon = { Icon(DzrIcons.PlayArrow, contentDescription = null) },
                    trailingText = { Text("Trailing") },
                    trailingIcon = { Icon(DzrIcons.MoreVert, contentDescription = null) },
                    enabled = false,
                    onClick = {}
                )
            }
        }
    }
}
