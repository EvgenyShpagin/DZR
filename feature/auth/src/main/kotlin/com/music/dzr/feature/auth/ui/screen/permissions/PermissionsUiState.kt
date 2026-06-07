package com.music.dzr.feature.auth.ui.screen.permissions

import com.music.dzr.core.mvi.UiState

data class PermissionsUiState(
    val permissions: List<PermissionUiState> = emptyList()
) : UiState
