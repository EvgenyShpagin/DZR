package com.music.dzr.core.data.mapper

import com.music.dzr.core.error.AppError
import com.music.dzr.core.error.ConnectivityError
import com.music.dzr.core.error.NetworkError
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.error.NetworkErrorType
import com.music.dzr.core.result.Result
import com.music.dzr.core.network.dto.error.NetworkError as NetworkErrorDto


/**
 * Converts a [NetworkResponse] to a [Result], mapping the network DTOs of type [I]
 * to domain-level data of type [O] and error models to one of [ConnectivityError] or [NetworkError].
 */
fun <I, O> NetworkResponse<I>.toResult(
    mapContent: (I) -> O
): Result<O, AppError> {
    // This assumes that if `error` is null, `data` is non-null.
    // A null `data` in a success scenario might indicate a problem.
    return if (error == null && data != null) {
        dataAsResult(map = mapContent)
    } else if (error != null) {
        errorAsResult()
    } else {
        Result.Failure(NetworkError.Unknown(description = "Data and error are null"))
    }
}

/**
 * A convenience overload of [toResult] that performs an identity mapping.
 *
 * Use this function when the network response type [T] is the same as the desired
 * domain type and no transformation is needed. It wraps the successful response data
 * or a network error directly into a [Result].
 */
fun <T> NetworkResponse<T>.toResult(): Result<T, AppError> {
    return toResult { it }
}

/**
 * Creates a [Result.Success] from the [NetworkResponse.data] field by executing [map] function.
 */
private fun <I, O> NetworkResponse<I>.dataAsResult(
    map: (I) -> O
): Result.Success<O> {
    return Result.Success(
        data = map(data!!)
    )
}

/**
 * Creates a [Result.Failure] from the [NetworkResponse.error] field.
 * Requires error to be non-null.
 */
private fun NetworkResponse<*>.errorAsResult(): Result.Failure<AppError> {
    val error = requireNotNull(error)
    val appError = error.toDomain()
    return Result.Failure(appError)
}
