package com.music.dzr.feature.auth.ui.screen.welcome

import com.music.dzr.core.mvi.UiEffect

sealed interface WelcomeUiEffect : UiEffect {
    data class LoginRequested(val authUrl: String) : WelcomeUiEffect
    data object RestrictAccessRequested : WelcomeUiEffect
}
