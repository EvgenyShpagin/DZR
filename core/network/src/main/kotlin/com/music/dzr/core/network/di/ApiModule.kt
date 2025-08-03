package com.music.dzr.core.network.di

import com.music.dzr.core.network.BuildConfig
import com.music.dzr.core.network.api.AuthApi
import com.music.dzr.core.network.api.MarketApi
import com.music.dzr.core.network.api.SearchApi
import com.music.dzr.core.network.util.AuthorizationUrlBuilder
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

internal val apiModule = module {

    single { AuthorizationUrlBuilder(BuildConfig.SPOTIFY_CLIENT_ID) }

    singleApi<AuthApi>(AUTH_RETROFIT)
    singleApi<MarketApi>(API_RETROFIT)
    singleApi<SearchApi>(API_RETROFIT)
}

private inline fun <reified T> Module.singleApi(retrofitName: String): KoinDefinition<T> {
    return single<T> { get<Retrofit>(named(retrofitName)).create() }
}