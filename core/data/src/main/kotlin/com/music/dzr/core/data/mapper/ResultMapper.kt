package com.music.dzr.core.data.mapper

import com.music.dzr.core.error.AppError
import com.music.dzr.core.error.ConnectivityError
import com.music.dzr.core.error.NetworkError
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.error.NetworkErrorType
import com.music.dzr.core.result.Result


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
    } else {
        errorAsResult()
    }
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
 */
private fun NetworkResponse<*>.errorAsResult(): Result.Failure<AppError> {
    val error = error!!
    val appError = when (error.type) {
        NetworkErrorType.Timeout -> ConnectivityError.Timeout
        NetworkErrorType.UnknownHost -> ConnectivityError.NoInternet
        NetworkErrorType.UnreachableHost,
        NetworkErrorType.SomeConnectionError,
        NetworkErrorType.SslException -> ConnectivityError.HostUnreachable

        NetworkErrorType.HttpException -> when (error.code) {
            401 -> NetworkError.Unauthorized
            403 -> NetworkError.InsufficientPermissions
            404 -> NetworkError.NotFound
            in 500..599 -> NetworkError.ServerError
            else -> NetworkError.Unknown(description = error.toString())
        }

        // Serialization and Unknown are mapped to a generic Unknown,
        // as they represent unexpected issues.
        NetworkErrorType.SerializationError,
        NetworkErrorType.Unknown -> NetworkError.Unknown(description = error.toString())
    }

    return Result.Failure(appError)
}
