package com.music.dzr.core.network.api

import com.music.dzr.core.network.AssetManager
import com.music.dzr.core.network.retrofit.NetworkErrorResponseParser
import com.music.dzr.core.network.retrofit.NetworkResponseCallAdapterFactory
import com.music.dzr.core.network.retrofit.UrlParameterConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

/**
 * Enqueues a response from a JSON file in the assets folder.
 */
internal fun MockWebServer.enqueueResponseFromAssets(assetFilename: String, code: Int = 200) {
    val responseBody = getJsonBodyAsset(assetFilename)
    enqueue(
        MockResponse()
            .setResponseCode(code)
            .setBody(responseBody)
    )
}

/**
 * Enqueues an empty response with a given HTTP status code and headers.
 */
internal fun MockWebServer.enqueueEmptyResponse(
    code: Int = 200,
    headers: Map<String, String> = emptyMap()
) {
    enqueue(
        MockResponse()
            .setResponseCode(code)
            .setBody("")
            .apply {
                headers.forEach { (name, value) ->
                    addHeader(name, value)
                }
            }
    )
}

/**
 * Converts a list of strings into a JSON array formatted string.
 * For example, `listOf("a", "b")` will be converted to `["a","b"]`.
 */
internal fun List<String>.toJsonArray(): String {
    val builder = StringBuilder()
    builder.append('[')
    forEach {
        builder.append("\"$it\"")
        if (it != last()) {
            builder.append(",")
        }
    }
    builder.append(']')
    return builder.toString()
}

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
        UrlParameterConverterFactory()
    )
    .addConverterFactory(
        json.asConverterFactory("application/json".toMediaType())
    )
    .addCallAdapterFactory(
        NetworkResponseCallAdapterFactory(errorResponseParser)
    )
    .build()
