package com.music.dzr.feature.auth.ui.screen.welcome

import com.music.dzr.core.mvi.UiState

data class WelcomeUiState(
    val coverMarqueeItems: List<WelcomeCoverMarqueeItemUiState> = emptyList(),
    val isLoading: Boolean = true
) : UiState
