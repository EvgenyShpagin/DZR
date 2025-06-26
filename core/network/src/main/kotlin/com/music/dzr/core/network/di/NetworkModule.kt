package com.music.dzr.core.network.di

import org.koin.dsl.module

/**
 * A Koin module that encapsulates all dependencies from the core:network.
 * This is the single entry point for other modules to include all network-related dependencies.
 */
internal val networkModule = module {
    includes(
        httpClientModule,
        retrofitModule,
        apiModule
    )
}