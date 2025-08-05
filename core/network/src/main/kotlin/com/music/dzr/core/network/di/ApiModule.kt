package com.music.dzr.core.network.di

import com.music.dzr.core.network.BuildConfig
import com.music.dzr.core.network.util.AuthorizationUrlBuilder
import org.koin.dsl.module

internal val apiModule = module {
    single { AuthorizationUrlBuilder(BuildConfig.SPOTIFY_CLIENT_ID) }
}
