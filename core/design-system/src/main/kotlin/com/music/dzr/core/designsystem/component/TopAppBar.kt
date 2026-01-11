package com.music.dzr.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.music.dzr.core.designsystem.R
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DzrTopAppBar(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    actionIcon: ImageVector? = null,
    actionIconContentDescription: String? = null,
    colors: TopAppBarColors = DzrTopAppBarDefaults.colors,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        navigationIcon = {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    positioning = TooltipAnchorPosition.Below
                ),
                tooltip = {
                    PlainTooltip {
                        Text(stringResource(R.string.core_design_system_navigate_up))
                    }
                },
                state = rememberTooltipState(),
            ) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = DzrIcons.ArrowBack,
                        contentDescription = stringResource(R.string.core_design_system_navigate_up),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
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
        scrollBehavior = scrollBehavior,
        modifier = modifier.testTag("dzrTopAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun DzrTopAppBarPreview() {
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

@OptIn(ExperimentalMaterial3Api::class)
object DzrTopAppBarDefaults {
    val colors
        @Composable get() = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0f),
            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
        )
}
