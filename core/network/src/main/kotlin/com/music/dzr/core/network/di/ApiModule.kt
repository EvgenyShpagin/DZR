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
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

internal val apiModule = module {

    single { AuthorizationUrlBuilder(BuildConfig.SPOTIFY_CLIENT_ID) }

    single<AuthApi> { get<Retrofit>(named(AUTH_RETROFIT)).create() }

    single<AlbumApi> { get<Retrofit>(named(API_RETROFIT)).create() }
    single<ArtistApi> { get<Retrofit>(named(API_RETROFIT)).create() }
    single<BrowseCategoryApi> { get<Retrofit>(named(API_RETROFIT)).create() }
    single<MarketApi> { get<Retrofit>(named(API_RETROFIT)).create() }
    single<PlayerApi> { get<Retrofit>(named(API_RETROFIT)).create() }
    single<PlaylistApi> { get<Retrofit>(named(API_RETROFIT)).create() }
    single<SearchApi> { get<Retrofit>(named(API_RETROFIT)).create() }
    single<TrackApi> { get<Retrofit>(named(API_RETROFIT)).create() }
    single<UserApi> { get<Retrofit>(named(API_RETROFIT)).create() }
}
