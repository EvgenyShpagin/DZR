package com.music.dzr.core.network.http

import com.music.dzr.core.network.api.enqueueEmptyResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RateLimitInterceptorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: OkHttpClient
    private lateinit var request: Request

    @BeforeTest
    fun setup() {
        mockWebServer = MockWebServer().apply {
            start()
        }
        val interceptor = RateLimitInterceptor(maxRetries = 2, baseDelayMs = 10)
        client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .build()
    }

    @AfterTest
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun retriesAndSucceeds_when429WithRetryAfterHeader() {
        // Given
        mockWebServer.enqueueEmptyResponse(
            code = 429,
            headers = mapOf("Retry-After" to "1")
        )
        mockWebServer.enqueueEmptyResponse()

        val startTime = System.currentTimeMillis()

        // When
        val response = client.newCall(request).execute()

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // Then
        assertEquals(200, response.code)
        assertTrue(duration >= 1000) // Should have waited at least 1 second (Retry-After)
        assertEquals(2, mockWebServer.requestCount) // Original + 1 retry
        response.close()
    }

    @Test
    fun retriesMultipleTimesAndSucceeds_whenMultiple429Responses() {
        // Given
        mockWebServer.enqueueEmptyResponse(code = 429)
        mockWebServer.enqueueEmptyResponse(code = 429)
        mockWebServer.enqueueEmptyResponse()

        // When
        val response = client.newCall(request).execute()

        // Then
        assertEquals(200, response.code)
        assertEquals(3, mockWebServer.requestCount) // Original + 2 retries
        response.close()
    }

    @Test
    fun exhaustsRetriesAndReturns429_whenAllResponsesRateLimited() {
        // Given
        repeat(5) {
            mockWebServer.enqueueEmptyResponse(code = 429)
        }

        // When
        val response = client.newCall(request).execute()

        // Then
        assertEquals(429, response.code)
        assertEquals(3, mockWebServer.requestCount) // Original + 2 retries (maxRetries = 2)
        response.close()
    }

    @Test
    fun doesNotRetry_whenNon429Error() {
        // Given
        mockWebServer.enqueueEmptyResponse(code = 500)

        // When
        val response = client.newCall(request).execute()

        // Then
        assertEquals(500, response.code)
        assertEquals(1, mockWebServer.requestCount) // No retries
        response.close()
    }

    @Test
    fun usesExponentialBackoff_whenRetryAfterHeaderMissing() {
        // Given
        mockWebServer.enqueueEmptyResponse(code = 429)
        mockWebServer.enqueueEmptyResponse()

        val startTime = System.currentTimeMillis()

        // When
        val response = client.newCall(request).execute()

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // Then
        assertEquals(200, response.code)
        assertEquals(2, mockWebServer.requestCount) // Original + 1 retry
        assertTrue(duration >= 10) // Should have waited at least baseDelayMs
        response.close()
    }

    @Test
    fun waitsCorrectTime_whenRetryAfterHeaderPresent() {
        // Given
        mockWebServer.enqueueEmptyResponse(
            code = 429,
            headers = mapOf("Retry-After" to "1")
        )
        mockWebServer.enqueueEmptyResponse()

        val startTime = System.currentTimeMillis()

        // When
        val response = client.newCall(request).execute()

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // Then
        assertEquals(200, response.code)
        assertTrue(duration >= 1000) // Should have waited at least 1 second
        assertTrue(duration < 2000) // Should not have waited much more
        assertEquals(2, mockWebServer.requestCount) // Original + 1 retry
        response.close()
    }

    @Test
    fun handlesSequentialRequests_whenSomeRateLimited() {
        // Given
        // First request: rate limited then success
        mockWebServer.enqueueEmptyResponse(
            code = 429,
            headers = mapOf("Retry-After" to "1")
        )
        mockWebServer.enqueueEmptyResponse()
        // Second request: immediate success
        mockWebServer.enqueueEmptyResponse()

        val request1 = Request.Builder()
            .url(mockWebServer.url("/test1"))
            .build()
        val request2 = Request.Builder()
            .url(mockWebServer.url("/test2"))
            .build()

        val startTime = System.currentTimeMillis()

        // When
        val response1 = client.newCall(request1).execute()
        val response2 = client.newCall(request2).execute()

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // Then
        assertEquals(200, response1.code)
        assertEquals(200, response2.code)
        assertEquals(3, mockWebServer.requestCount) // 2 for first request, 1 for second
        assertTrue(duration >= 1000) // Should have waited for first request

        response1.close()
        response2.close()
    }

    @Test
    fun fallsBackToExponentialBackoff_whenRetryAfterInvalid() {
        // Given
        mockWebServer.enqueueEmptyResponse(
            code = 429,
            headers = mapOf("Retry-After" to "invalid")
        )
        mockWebServer.enqueueEmptyResponse()

        // When
        val response = client.newCall(request).execute()

        // Then
        assertEquals(200, response.code)
        assertEquals(2, mockWebServer.requestCount) // Original + 1 retry
        response.close()
    }

    @Test
    fun handlesEmptyRetryAfterHeader_whenPresent() {
        // Given
        mockWebServer.enqueueEmptyResponse(
            code = 429,
            headers = mapOf("Retry-After" to "")
        )
        mockWebServer.enqueueEmptyResponse()

        // When
        val response = client.newCall(request).execute()

        // Then
        assertEquals(200, response.code)
        assertEquals(2, mockWebServer.requestCount) // Original + 1 retry
        response.close()
    }
}