package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.component.DzrButton
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.feature.auth.R

@Composable
internal fun WelcomeLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DzrButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        text = {
            Text(
                text = stringResource(R.string.feature_auth_welcome_login),
                style = MaterialTheme.typography.titleMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = DzrIcons.Login,
                contentDescription = null
            )
        }
    )
}


@PreviewLightDark
@Composable
private fun WelcomeLoginButtonPreview() {
    DzrTheme {
        WelcomeLoginButton(
            onClick = {},
        )
    }
}
