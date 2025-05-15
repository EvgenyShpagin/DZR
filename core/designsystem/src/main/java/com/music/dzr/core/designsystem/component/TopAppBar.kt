package com.music.dzr.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DzrTopAppBar(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    actionIcon: ImageVector? = null,
    actionIconContentDescription: String? = null,
    colors: TopAppBarColors = DzrTopAppBarDefaults.colors,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = DzrIcons.ArrowBack,
                    contentDescription = stringResource(R.string.cd_navigation_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIconContentDescription,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        colors = colors,
        modifier = modifier.testTag("dzrTopAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun DzrTopAppBarPreview() {
    DzrTheme {
        DzrTopAppBar(
            titleRes = android.R.string.untitled,
            actionIcon = DzrIcons.MoreVert,
            actionIconContentDescription = "Action icon",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
object DzrTopAppBarDefaults {
    val colors
        @Composable get() = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
        )
}
