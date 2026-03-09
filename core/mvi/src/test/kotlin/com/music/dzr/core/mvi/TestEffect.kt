package com.music.dzr.core.mvi

/**
 * A testing implementation of [UiEffect] used for verifying side effect emission
 * and capacity constraints in the MVI view model tests.
 */
internal sealed interface TestEffect : UiEffect {
    data object Navigate : TestEffect
    data object ShowToast : TestEffect
}
