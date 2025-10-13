package com.music.dzr.core.data.test

import com.music.dzr.core.error.AppError
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.result.Result

/**
 * A marker interface for test fakes that can simulate returning a forced error.
 */
interface HasForcedError<E> {
    /**
     * The error to be returned by the test double instead of the normal data.
     *
     * When this is non-null, helper functions like [respond] should return this error
     * instead of executing the normal logic.
     */
    var forcedError: E?
}

/**
 * Wraps a computation in a [Result], returning [HasForcedError.forcedError]
 * if it's present, or the computed data otherwise.
 */
inline fun <D, E : AppError> HasForcedError<AppError>.runUnlessForcedError(
    block: () -> Result<D, E>
): Result<D, E> {
    val error = forcedError
    return if (error != null) {
        @Suppress("UNCHECKED_CAST")
        Result.Failure(error as E)
    } else {
        block()
    }
}

/**
 * Wraps a computation in a [Result], returning [HasForcedError.forcedError]
 * if it's present, or the computed data otherwise.
 */
inline fun <T> HasForcedError<NetworkError>.runUnlessForcedError(
    block: () -> T
): NetworkResponse<T> {
    val error = forcedError
    return if (error != null) {
        NetworkResponse(error = error)
    } else {
        NetworkResponse(data = block())
    }
}
