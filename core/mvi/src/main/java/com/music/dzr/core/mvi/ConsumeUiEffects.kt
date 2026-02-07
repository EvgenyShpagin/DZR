package com.music.dzr.core.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <E : UiEffect> Flow<E>.consumeWithLifecycle(onReceive: suspend (E) -> Unit) {
    val uiEffect = rememberFlowWithLifecycle(this)
    val currentOnReceive by rememberUpdatedState(onReceive)
    LaunchedEffect(uiEffect) {
        uiEffect.collect { currentOnReceive(it) }
    }
}

@Composable
private fun <T> rememberFlowWithLifecycle(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle
): Flow<T> {
    return remember(flow, lifecycle) {
        flow.flowWithLifecycle(
            lifecycle,
            Lifecycle.State.STARTED
        )
    }
}
