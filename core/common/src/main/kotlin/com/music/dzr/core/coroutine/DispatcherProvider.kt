package com.music.dzr.core.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Abstraction for providing coroutine dispatchers.
 *
 * Allows dependency inversion and easy dispatcher substitution in tests.
 * Production code uses real dispatchers ([Dispatchers][kotlinx.coroutines.Dispatchers]),
 * tests use test dispatchers for controlled execution.
 */
interface DispatcherProvider {
    /**
     * Dispatcher for I/O operations (network, database, file system).
     */
    val io: CoroutineDispatcher

    /**
     * Dispatcher for CPU-intensive tasks.
     */
    val default: CoroutineDispatcher

    /**
     * Dispatcher for UI work (main thread).
     */
    val main: CoroutineDispatcher
}
