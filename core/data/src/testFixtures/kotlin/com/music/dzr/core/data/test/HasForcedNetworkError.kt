package com.music.dzr.core.data.test

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.error.NetworkError

/**
 * Marker interface for fake remote data sources that can simulate an error.
 *
 * Implementations expose a mutable [forcedError] that, when set, should be
 * returned instead of normal data in helper functions like [respond].
 */
interface HasForcedNetworkError {
    /**
     * Optional network error to be returned by fake sources instead of data.
     *
     * When non-null, helper wrappers (e.g., [respond]) should construct
     * a [NetworkResponse] containing this error and skip executing the body.
     */
    var forcedError: NetworkError?
}

/**
 * Wraps a computation into a [NetworkResponse], returning [HasForcedNetworkError.forcedError]
 * when it is present or the computed data otherwise.
 */
inline fun <T> HasForcedNetworkError.respond(block: () -> T): NetworkResponse<T> {
    val error = forcedError
    return if (error != null) {
        NetworkResponse(error = error)
    } else {
        NetworkResponse(data = block())
    }
}