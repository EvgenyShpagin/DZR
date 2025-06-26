package com.music.dzr.core.network.di

import com.music.dzr.core.network.BuildConfig
import com.music.dzr.core.network.retrofit.NetworkErrorResponseParser
import com.music.dzr.core.network.retrofit.NetworkResponseCallAdapterFactory
import com.music.dzr.core.network.retrofit.UrlParameterConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

internal const val API_RETROFIT = "ApiRetrofit"
internal const val AUTH_RETROFIT = "AuthRetrofit"

private const val JSON_CONVERTER_FACTORY = "JsonConverterFactory"
private const val URL_PARAM_CONVERTER_FACTORY = "UrlParamConverterFactory"

internal val retrofitModule = module {

    single { Json { ignoreUnknownKeys = true } }

    single { NetworkErrorResponseParser(get()) }

    single { NetworkResponseCallAdapterFactory(get()) }

    single(named(JSON_CONVERTER_FACTORY)) {
        get<Json>().asConverterFactory("application/json".toMediaType())
    }

    single(named(URL_PARAM_CONVERTER_FACTORY)) {
        UrlParameterConverterFactory()
    }

    single(named(AUTH_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SPOTIFY_ACCOUNTS_URL)
            .client(get(named(AUTH_CLIENT)))
            .addConverterFactory(get(named(JSON_CONVERTER_FACTORY)))
            .addCallAdapterFactory(get<NetworkResponseCallAdapterFactory>())
            .build()
    }

    single(named(API_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SPOTIFY_API_URL)
            .client(get(named(API_CLIENT)))
            .addConverterFactory(get(named(URL_PARAM_CONVERTER_FACTORY)))
            .addConverterFactory(get(named(JSON_CONVERTER_FACTORY)))
            .addCallAdapterFactory(get<NetworkResponseCallAdapterFactory>())
            .build()
    }
} 