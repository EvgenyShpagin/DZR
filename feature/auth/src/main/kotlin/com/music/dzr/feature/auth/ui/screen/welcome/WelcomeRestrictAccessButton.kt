package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.component.DzrOutlinedButton
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.feature.auth.R

@Composable
internal fun WelcomeRestrictAccessButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DzrOutlinedButton(
        onClick = onClick,
        modifier = modifier
            .padding(vertical = 8.dp)
            .height(36.dp),
        text = {
            Text(
                text = stringResource(R.string.feature_auth_welcome_restrict_access),
                style = MaterialTheme.typography.labelLarge
            )
        },
        leadingIcon = {
            Icon(
                imageVector = DzrIcons.LockPerson,
                contentDescription = null
            )
        }
    )
}

@PreviewLightDark
@Composable
private fun WelcomeRestrictAccessButtonPreview() {
    DzrTheme {
        Surface {
            WelcomeRestrictAccessButton(onClick = {})
        }
    }
}
