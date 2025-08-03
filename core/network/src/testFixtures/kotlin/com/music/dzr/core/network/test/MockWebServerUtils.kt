package com.music.dzr.core.network.test

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

/**
 * Enqueues a response from a JSON file in the assets folder.
 */
fun MockWebServer.enqueueResponseFromAssets(assetFilename: String, code: Int = 200) {
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
fun MockWebServer.enqueueEmptyResponse(
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
