package com.music.dzr.feature.auth.ui.screen.permissions

import com.music.dzr.core.mvi.UiEvent

sealed class PermissionUiEvent : UiEvent(Immediate) {
    data class PermissionToggled(
        val permission: PermissionUiState
    ) : PermissionUiEvent()

    data object ToggleAllClicked : PermissionUiEvent()
    data object SaveClicked : PermissionUiEvent()
    data object BackClicked : PermissionUiEvent()
}
