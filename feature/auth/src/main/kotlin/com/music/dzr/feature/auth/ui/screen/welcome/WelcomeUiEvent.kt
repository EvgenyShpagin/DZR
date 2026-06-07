package com.music.dzr.feature.auth.ui.screen.welcome

import com.music.dzr.core.mvi.UiEvent

sealed class WelcomeUiEvent(policy: Policy) : UiEvent(policy) {
    data object LoginClicked : WelcomeUiEvent(policy = ThrottleFirst(scope = Navigation))
    data object RestrictAccessClicked : WelcomeUiEvent(policy = ThrottleFirst(scope = Navigation))
    data class ReturnedFromBrowser(val uri: String) : WelcomeUiEvent(
        policy = ThrottleFirst(scope = Navigation)
    )
}
