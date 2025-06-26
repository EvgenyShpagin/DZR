package com.music.dzr.core.network.di

/**
 * A list of all Koin modules from the core:network module.
 * This is the single entry point for other modules to include all network-related dependencies.
 */
internal val networkModule = listOf(
    httpClientModule,
    retrofitModule,
    apiModule
)