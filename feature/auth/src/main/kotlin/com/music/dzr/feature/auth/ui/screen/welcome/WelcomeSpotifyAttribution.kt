package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.feature.auth.R

private val AttributionLogoWidth = 85.dp
private val AttributionLogoHeight = 23.dp
private val AttributionLogoBaseline = 5.75.dp


@Composable
internal fun WelcomeSpotifyAttribution(
    modifier: Modifier = Modifier
) {
    val textStyle = MaterialTheme.typography.bodyMedium
    val fontScale = LocalDensity.current.fontScale
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            text = stringResource(R.string.feature_auth_welcome_powered_by_label),
            style = textStyle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.paddingFromBaseline(
                bottom = AttributionLogoBaseline * fontScale
            )
        )
        Spacer(Modifier.width(4.dp))
        Icon(
            painter = painterResource(R.drawable.feature_auth_spotify_logo),
            contentDescription = null,
            modifier = Modifier.size(
                width = AttributionLogoWidth * fontScale,
                height = AttributionLogoHeight * fontScale
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun WelcomeSpotifyAttributionPreview() {
    DzrTheme {
        Surface {
            WelcomeSpotifyAttribution()
        }
    }
}
