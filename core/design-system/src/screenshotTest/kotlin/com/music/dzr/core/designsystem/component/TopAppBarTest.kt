package com.music.dzr.core.designsystem.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@OptIn(ExperimentalMaterial3Api::class)
@PreviewTest
@ThemeAndFontScalePreviews
@Composable
private fun DzrTopAppBar_Preview() {
    DzrTheme {
        Surface {
            DzrTopAppBar(
                titleRes = android.R.string.untitled,
                actionIcon = DzrIcons.MoreVert,
                actionIconContentDescription = "Action icon",
            )
        }
    }
}
