package com.music.dzr.core.network.di

import com.music.dzr.core.network.BuildConfig
import com.music.dzr.core.network.api.AlbumApi
import com.music.dzr.core.network.api.ArtistApi
import com.music.dzr.core.network.api.BrowseCategoryApi
import com.music.dzr.core.network.api.MarketApi
import com.music.dzr.core.network.api.PlayerApi
import com.music.dzr.core.network.api.PlaylistApi
import com.music.dzr.core.network.api.SearchApi
import com.music.dzr.core.network.api.TrackApi
import com.music.dzr.core.network.api.UserApi
import com.music.dzr.core.network.retrofit.NetworkErrorResponseParser
import com.music.dzr.core.network.retrofit.NetworkResponseCallAdapterFactory
import com.music.dzr.core.network.retrofit.UrlParameterConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

private const val API_RETROFIT = "ApiRetrofit"

private const val JSON_CONVERTER_FACTORY = "JsonConverterFactory"
private const val URL_PARAM_CONVERTER_FACTORY = "UrlParamConverterFactory"

val networkModule = module {

    single { Json { ignoreUnknownKeys = true } }

    single { NetworkErrorResponseParser(get()) }

    single(named(JSON_CONVERTER_FACTORY)) {
        get<Json>().asConverterFactory("application/json".toMediaType())
    }

    single(named(URL_PARAM_CONVERTER_FACTORY)) {
        UrlParameterConverterFactory()
    }

    single { NetworkResponseCallAdapterFactory(get()) }

    single(named(API_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SPOTIFY_API_URL)
            .addConverterFactory(get(named(URL_PARAM_CONVERTER_FACTORY)))
            .addConverterFactory(get(named(JSON_CONVERTER_FACTORY)))
            .addCallAdapterFactory(get<NetworkResponseCallAdapterFactory>())
            .build()
    }

    // API Implementations
    single { get<Retrofit>().create<AlbumApi>() }
    single { get<Retrofit>().create<ArtistApi>() }
    single { get<Retrofit>().create<BrowseCategoryApi>() }
    single { get<Retrofit>().create<MarketApi>() }
    single { get<Retrofit>().create<PlayerApi>() }
    single { get<Retrofit>().create<PlaylistApi>() }
    single { get<Retrofit>().create<SearchApi>() }
    single { get<Retrofit>().create<TrackApi>() }
    single { get<Retrofit>().create<UserApi>() }
}