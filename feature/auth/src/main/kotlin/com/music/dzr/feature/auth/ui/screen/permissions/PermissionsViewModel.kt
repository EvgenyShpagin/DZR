package com.music.dzr.feature.auth.ui.screen.permissions

import com.music.dzr.core.auth.domain.model.PermissionScope
import com.music.dzr.core.mvi.MutableStateViewModel

class PermissionsViewModel : MutableStateViewModel<
        PermissionsUiState,
        PermissionUiEvent,
        PermissionUiEffect>(initialUiState = initialUiState) {

    override suspend fun handleEvent(event: PermissionUiEvent) {
        when (event) {
            PermissionUiEvent.ToggleAllClicked -> {
                updateState { state ->
                    val permissions = state.permissions
                    val allGranted = permissions.all { it.isGranted }
                    state.copy(permissions = permissions.map { it.copy(isGranted = !allGranted) })
                }
            }

            is PermissionUiEvent.PermissionToggled -> {
                updateState { state ->
                    state.copy(
                        permissions = state.permissions.map {
                            if (it.titleRes == event.permission.titleRes) {
                                it.copy(isGranted = !it.isGranted)
                            } else {
                                it
                            }
                        }
                    )
                }
            }

            PermissionUiEvent.BackClicked -> triggerEffect(PermissionUiEffect.Dismissed)
            PermissionUiEvent.SaveClicked -> triggerEffect(PermissionUiEffect.PermissionsSaved)
        }
    }

    companion object {
        private val initialUiState = PermissionsUiState(
            permissions = PermissionScope.entries.map {
                it.toUiState(isGranted = true)
            }
        )
    }
}
