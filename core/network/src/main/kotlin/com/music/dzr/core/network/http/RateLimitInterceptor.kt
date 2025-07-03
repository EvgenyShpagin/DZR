package com.music.dzr.core.network.http

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

/**
 * Interceptor for handling rate limiting (429 errors) according to Spotify API documentation
 * [Rate Limits](https://developer.spotify.com/documentation/web-api/concepts/rate-limits)
 *
 * Spotify's API rate limit is calculated based on the number of calls that your app makes
 * to Spotify in a rolling 30 second window. When rate limited, the API returns HTTP 429
 * with a Retry-After header specifying how long to wait before making another request.
 */
internal class RateLimitInterceptor(
    private val maxRetries: Int = 3,
    private val baseDelayMs: Long = 1000L,
    private val maxDelayMs: Long = 32000L,
    private val jitterFactor: Double = 0.1
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        var retryCount = 0

        while (response.code == 429 && retryCount < maxRetries) {
            val retryAfter = extractRetryAfterSeconds(response)
            val delayMs = calculateDelayMillis(retryCount, retryAfter)

            response.close()

            runBlocking {
                delay(delayMs)
            }

            response = chain.proceed(request)
            ++retryCount
        }

        return response
    }

    /**
     * Extracts the Retry-After value from response headers
     * According to Spotify docs, this header specifies how long to wait before making another request
     */
    private fun extractRetryAfterSeconds(response: Response): Long? {
        val retryAfterHeader = response.header("Retry-After")
        val retryAfter = retryAfterHeader?.toLongOrNull() ?: return null
        return TimeUnit.SECONDS.toMillis(retryAfter)
    }

    /**
     * Calculates delay for retry attempts
     * Uses exponential backoff with jitter to avoid thundering herd problem
     *
     * Priority order:
     * 1. Use Retry-After header if present (recommended by Spotify)
     * 2. Fall back to exponential backoff with jitter
     */
    @VisibleForTesting
    fun calculateDelayMillis(retryCount: Int, retryAfter: Long?): Long {
        // If Retry-After header is present, use it (Spotify's recommendation)
        retryAfter?.let { return it }

        // Exponential backoff: baseDelay * (2^retryCount)
        val exponentialDelay = baseDelayMs * 2.0.pow(retryCount).toLong()
        val cappedDelay = min(exponentialDelay, maxDelayMs)

        // Add jitter to prevent thundering herd effect
        val jitter = cappedDelay * jitterFactor * Random.nextDouble()

        return (cappedDelay + jitter).toLong()
    }
}