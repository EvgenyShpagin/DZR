package com.music.dzr.core.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * Collects one-time [UiEffect]s from this [Flow] in a lifecycle-aware manner.
 *
 * Collection is active only while the [lifecycleOwner] is at least in [minActiveState].
 * Uses [repeatOnLifecycle] internally — the same pattern as `collectAsStateWithLifecycle`.
 */
@Suppress("ComposableNaming")
@Composable
fun <E : UiEffect> Flow<E>.consumeWithLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    onReceive: suspend (E) -> Unit
) {
    val currentOnReceive by rememberUpdatedState(onReceive)
    LaunchedEffect(this, lifecycleOwner.lifecycle) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            this@consumeWithLifecycle.collect { currentOnReceive(it) }
        }
    }
}
