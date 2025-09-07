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

private val UrlParamConverterFactoryQualifier = named("UrlParamConverterFactory")

internal val retrofitModule = module {

    single { Json { ignoreUnknownKeys = true } }

    single { NetworkErrorResponseParser(get()) }

    single(NetworkResponseCallAdapterFactoryQualifier) {
        NetworkResponseCallAdapterFactory(get())
    }

    single(JsonConverterFactoryQualifier) {
        get<Json>().asConverterFactory("application/json".toMediaType())
    }

    single(UrlParamConverterFactoryQualifier) {
        UrlParameterConverterFactory()
    }

    single(ApiRetrofitQualifier) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SPOTIFY_API_URL)
            .client(get(ApiClientQualifier))
            .addConverterFactory(get(UrlParamConverterFactoryQualifier))
            .addConverterFactory(get(JsonConverterFactoryQualifier))
            .addCallAdapterFactory(get(NetworkResponseCallAdapterFactoryQualifier))
            .build()
    }
}