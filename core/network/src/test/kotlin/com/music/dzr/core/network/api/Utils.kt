package com.music.dzr.core.network.api

import com.music.dzr.core.network.AssetManager
import com.music.dzr.core.network.retrofit.NetworkErrorResponseParser
import com.music.dzr.core.network.retrofit.NetworkResponseCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

/**
 * Helper function to get response json body from assets file
 */
internal fun getJsonBodyAsset(filename: String): String {
    return AssetManager.open(filename).use { inputStream ->
        inputStream.bufferedReader().use { it.readText() }
    }
}

/**
 * Helper function to create Retrofit instance within test
 */
internal inline fun <reified T : Any> createApi(
    httpUrl: HttpUrl,
    json: Json = Json { ignoreUnknownKeys = true },
    errorResponseParser: NetworkErrorResponseParser = NetworkErrorResponseParser(json)
): T {
    return createRetrofit(httpUrl, json, errorResponseParser).create<T>()
}

private fun createRetrofit(
    httpUrl: HttpUrl,
    json: Json,
    errorResponseParser: NetworkErrorResponseParser
) = Retrofit.Builder()
    .baseUrl(httpUrl)
    .addConverterFactory(
        json.asConverterFactory("application/json".toMediaType())
    )
    .addCallAdapterFactory(
        NetworkResponseCallAdapterFactory(errorResponseParser)
    )
    .build()
