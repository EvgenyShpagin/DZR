package com.music.dzr.feature.auth.ui.screen.permissions

import com.music.dzr.core.mvi.UiEffect

sealed interface PermissionUiEffect : UiEffect {
    data object Dismissed : PermissionUiEffect
    data object PermissionsSaved : PermissionUiEffect
}
