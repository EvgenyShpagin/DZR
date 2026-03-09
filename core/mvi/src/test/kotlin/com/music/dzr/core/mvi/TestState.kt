package com.music.dzr.core.mvi

/**
 * A testing implementation of [UiState] used for verifying state updates
 * in the MVI view model tests.
 */
internal data class TestState(val value: Int = 0) : UiState
