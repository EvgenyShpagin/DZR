package com.music.dzr.core.network.di

import com.music.dzr.core.network.retrofit.NetworkErrorResponseParser
import com.music.dzr.core.network.retrofit.NetworkResponseCallAdapterFactory
import com.music.dzr.core.network.retrofit.UrlParameterConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val DZR_BASE_URL = "https://api.deezer.com/"

val networkModule = module {

    single {
        Json { ignoreUnknownKeys = true }
    }

    single {
        NetworkErrorResponseParser(get())
    }

    single {
        Retrofit.Builder()
            .baseUrl(DZR_BASE_URL)
            .addConverterFactory(UrlParameterConverterFactory())
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .addCallAdapterFactory(NetworkResponseCallAdapterFactory(get()))
            .build()
    }

}