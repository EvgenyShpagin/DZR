package com.music.dzr.core.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDark
@Composable
private fun GroupedList_Preview() {
    ExampleGroupedList()
}

@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun GroupedList_FontScale_1_5_Preview() {
    ExampleGroupedList()
}


@Composable
private fun ExampleGroupedList() {
    DzrTheme {
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