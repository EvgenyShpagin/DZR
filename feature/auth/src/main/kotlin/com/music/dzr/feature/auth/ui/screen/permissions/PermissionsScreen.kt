@file:OptIn(ExperimentalMaterial3Api::class)

package com.music.dzr.feature.auth.ui.screen.permissions

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.music.dzr.core.designsystem.component.DzrScaffold
import com.music.dzr.core.designsystem.theme.DzrBackground
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.mvi.consumeWithLifecycle

@Composable
fun PermissionsScreen(
    viewModel: PermissionsViewModel,
    onPermissionsSave: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.uiEffect.consumeWithLifecycle { effect ->
        when (effect) {
            PermissionUiEffect.PermissionsSaved -> onPermissionsSave()
            PermissionUiEffect.Dismissed -> onDismiss()
        }
    }

    PermissionsScreen(
        permissions = uiState.permissions,
        onSaveClick = { viewModel.onEvent(PermissionUiEvent.SaveClicked) },
        onToggleAllClick = {
            viewModel.onEvent(PermissionUiEvent.ToggleAllClicked)
        },
        onPermissionToggle = { permission ->
            viewModel.onEvent(PermissionUiEvent.PermissionToggled(permission))
        },
        onBackClick = { viewModel.onEvent(PermissionUiEvent.BackClicked) },
        modifier = modifier
    )
}

@VisibleForTesting
@Composable
fun PermissionsScreen(
    permissions: List<PermissionUiState>,
    onSaveClick: () -> Unit,
    onToggleAllClick: () -> Unit,
    onPermissionToggle: (PermissionUiState) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    val scrollState = rememberScrollState()

    DzrBackground(
        modifier = modifier,
        scrollState = scrollState
    ) {
        DzrScaffold(
            modifier = Modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            topBar = {
                PermissionsTopAppBar(
                    scrollBehavior = topAppBarScrollBehavior,
                    onNavigationClick = onBackClick,
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(bottom = 8.dp)
                ) {
                    PermissionsSaveButton(
                        onClick = onSaveClick,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    PermissionsCheckAllButton(
                        areAllChecked = permissions.all { it.isGranted },
                        onClick = onToggleAllClick,
                        modifier = Modifier.width(96.dp)
                    )
                }
            }
        ) { innerPadding ->
            PermissionList(
                items = permissions,
                onPermissionToggle = onPermissionToggle,
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    )
            )
        }
    }
}

@Preview
@Composable
private fun PermissionsScreenPreview() {
    DzrTheme {
        Surface {
            PermissionsScreen(
                permissions = previewPermissions,
                onSaveClick = {},
                onToggleAllClick = {},
                onPermissionToggle = {},
                onBackClick = {}
            )
        }
    }
}
