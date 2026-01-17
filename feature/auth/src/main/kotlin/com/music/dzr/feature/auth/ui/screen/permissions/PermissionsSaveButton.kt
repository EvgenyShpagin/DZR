package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.music.dzr.core.designsystem.component.DzrButton
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.feature.auth.R

@Composable
fun PermissionsSaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DzrButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(text = stringResource(R.string.feature_auth_permissions_save))
    }
}

@PreviewLightDark
@Composable
private fun PermissionsSaveButtonPreview() {
    DzrTheme {
        PermissionsSaveButton(onClick = {})
    }
}
