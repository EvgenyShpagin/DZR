package com.music.dzr.core.designsystem.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@OptIn(ExperimentalMaterial3Api::class)
@PreviewTest
@PreviewLightDark
@Composable
private fun DzrTopAppBar_Preview() {
    DzrTheme {
        DzrTopAppBar(
            titleRes = android.R.string.untitled,
            actionIcon = DzrIcons.MoreVert,
            actionIconContentDescription = "Action icon",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewTest
@Preview(fontScale = 1.5f)
@Composable
private fun DzrTopAppBar_FontScale_1_5_Preview() {
    DzrTheme {
        DzrTopAppBar(
            titleRes = android.R.string.untitled,
            actionIcon = DzrIcons.MoreVert,
            actionIconContentDescription = "Action icon",
        )
    }
} 