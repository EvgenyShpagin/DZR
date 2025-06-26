package com.music.dzr.core.network.di

import com.music.dzr.core.network.BuildConfig
import com.music.dzr.core.network.api.AlbumApi
import com.music.dzr.core.network.api.ArtistApi
import com.music.dzr.core.network.api.AuthApi
import com.music.dzr.core.network.api.BrowseCategoryApi
import com.music.dzr.core.network.api.MarketApi
import com.music.dzr.core.network.api.PlayerApi
import com.music.dzr.core.network.api.PlaylistApi
import com.music.dzr.core.network.api.SearchApi
import com.music.dzr.core.network.api.TrackApi
import com.music.dzr.core.network.api.UserApi
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

    singleApi<AlbumApi>(API_RETROFIT)
    singleApi<ArtistApi>(API_RETROFIT)
    singleApi<BrowseCategoryApi>(API_RETROFIT)
    singleApi<MarketApi>(API_RETROFIT)
    singleApi<PlayerApi>(API_RETROFIT)
    singleApi<PlaylistApi>(API_RETROFIT)
    singleApi<SearchApi>(API_RETROFIT)
    singleApi<TrackApi>(API_RETROFIT)
    singleApi<UserApi>(API_RETROFIT)
}

private inline fun <reified T> Module.singleApi(retrofitName: String): KoinDefinition<T> {
    return single<T> { get<Retrofit>(named(retrofitName)).create() }
}