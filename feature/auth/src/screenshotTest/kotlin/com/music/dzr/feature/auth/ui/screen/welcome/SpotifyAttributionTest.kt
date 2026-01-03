package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.component.PreviewLightDarkAndFontScale
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDarkAndFontScale
@Composable
private fun SpotifyAttribution_Preview() {
    DzrTheme {
        Surface {
            SpotifyAttribution()
        }
    }
}
