package com.music.dzr.core.network.test

import com.music.dzr.core.network.retrofit.NetworkErrorResponseParser
import com.music.dzr.core.network.retrofit.NetworkResponseCallAdapterFactory
import com.music.dzr.core.network.retrofit.UrlParameterConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

/**
 * Helper function to create Retrofit instance within test
 */
inline fun <reified T : Any> createApi(
    httpUrl: HttpUrl,
    json: Json = Json { ignoreUnknownKeys = true },
    errorResponseParser: NetworkErrorResponseParser = NetworkErrorResponseParser(json)
): T {
    return createRetrofit(httpUrl, json, errorResponseParser).create<T>()
}

fun createRetrofit(
    httpUrl: HttpUrl,
    json: Json,
    errorResponseParser: NetworkErrorResponseParser
): Retrofit = Retrofit.Builder()
    .baseUrl(httpUrl)
    .addConverterFactory(
        UrlParameterConverterFactory()
    )
    .addConverterFactory(
        json.asConverterFactory("application/json".toMediaType())
    )
    .addCallAdapterFactory(
        NetworkResponseCallAdapterFactory(errorResponseParser)
    )
    .build()