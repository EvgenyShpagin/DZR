package com.music.dzr.feature.auth.ui.screen.welcome

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.music.dzr.core.auth.domain.usecase.GetAuthUrlUseCase
import com.music.dzr.core.auth.domain.usecase.HandleAuthResponseUseCase
import com.music.dzr.core.mvi.ImmutableStateViewModel
import com.music.dzr.core.result.onFailure
import com.music.dzr.core.result.onSuccess
import com.music.dzr.feature.auth.ui.navigation.AuthDestination
import com.music.dzr.feature.auth.ui.screen.welcome.WelcomeUiEffect.LoginRequested
import com.music.dzr.feature.auth.ui.screen.welcome.WelcomeUiEffect.RestrictAccessRequested
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getAuthUrlUseCase: GetAuthUrlUseCase,
    private val handleAuthResponseUseCase: HandleAuthResponseUseCase,
//    private val getWelcomeImageUrls: GetWelcomeImageUrls,
) : ImmutableStateViewModel<WelcomeUiState, WelcomeUiEvent, WelcomeUiEffect>() {

    override val uiState: StateFlow<WelcomeUiState> = MutableStateFlow(WelcomeUiState())

    private val destination get() = savedStateHandle.toRoute<AuthDestination.Welcome>()
    private val permissions get() = destination.grantedPermissions.toList()

    override suspend fun handleEvent(event: WelcomeUiEvent) {
        when (event) {
            WelcomeUiEvent.LoginClicked -> {
                if (uiState.value.isLoading) return
//                updateState { it.copy(isLoading = true) }
                viewModelScope.launch {
                    getAuthUrlUseCase(permissions)
                        .onSuccess { url ->
                            triggerEffect(LoginRequested(url))
                        }
                        .onFailure {
                            // TODO: Handle error (show snackbar)
//                            updateState { it.copy(isLoading = false) }
                        }
                }
            }

            WelcomeUiEvent.RestrictAccessClicked -> {
                // HERE IS NO LOADING if (uiState.value.isLoading) return
                triggerEffect(RestrictAccessRequested)
            }

            is WelcomeUiEvent.ReturnedFromBrowser -> {
                viewModelScope.launch {
                    handleAuthResponseUseCase(event.uri)
                        .onSuccess {
//                            updateState { it.copy(isLoading = false) }
                            // TODO: Navigate to next screen?
                        }
                        .onFailure {
                            // TODO: Handle error
//                            updateState { it.copy(isLoading = false) }
                        }
                }
            }
        }
    }
}
