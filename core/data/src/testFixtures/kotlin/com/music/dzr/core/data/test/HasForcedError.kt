package com.music.dzr.core.data.test

import com.music.dzr.core.error.AppError
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.result.Result

/**
 * A marker interface for test doubles that can simulate returning a forced error.
 */
interface HasForcedError<E> {
    /**
     * The error to be returned by the test double instead of the normal data.
     *
     * When this is non-null, helper functions like [runUnlessForcedError] should return this error
     * instead of executing the normal logic.
     */
    var forcedError: E?

    /**
     * When true, the same forced error will be returned on every invocation
     * until you clear [forcedError] manually.
     */
    var isStickyForcedError: Boolean
}

/**
 * Wraps a computation in a [Result], returning [HasForcedError.forcedError]
 * if it's present, or the computed data otherwise.
 */
inline fun <D, E : AppError, EE : E> HasForcedError<E>.runUnlessForcedError(
    block: () -> Result<D, EE>
): Result<D, EE> {
    val error = forcedError
    val result = if (error != null) {
        if (!isStickyForcedError) {
            forcedError = null
        }
        Result.Failure(error)
    } else {
        block()
    }
    @Suppress("UNCHECKED_CAST")
    return result as Result<D, EE>
}

/**
 * Wraps a computation into a [NetworkResponse], returning [HasForcedError.forcedError]
 * when it is present or the computed data otherwise.
 */
inline fun <T> HasForcedError<NetworkError>.runUnlessForcedError(
    block: () -> T
): NetworkResponse<T> {
    val error = forcedError
    return if (error != null) {
        if (!isStickyForcedError) {
            forcedError = null
        }
        NetworkResponse(error = error)
    } else {
        NetworkResponse(data = block())
    }
}
