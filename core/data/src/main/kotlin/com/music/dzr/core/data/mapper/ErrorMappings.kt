package com.music.dzr.core.data.mapper

import com.music.dzr.core.error.AppError
import com.music.dzr.core.error.ConnectivityError
import com.music.dzr.core.error.NetworkError
import com.music.dzr.core.network.dto.error.NetworkErrorType
import com.music.dzr.core.network.dto.error.NetworkError as NetworkErrorDto

/**
 * Converts a network-layer error (DTO) into a domain-level [AppError].
 *
 * Returns [ConnectivityError] or [NetworkError].
 */
fun NetworkErrorDto.toDomain(): AppError {
    return when (this.type) {
        NetworkErrorType.Timeout -> ConnectivityError.Timeout
        NetworkErrorType.UnknownHost -> ConnectivityError.NoInternet
        NetworkErrorType.UnreachableHost,
        NetworkErrorType.SomeConnectionError,
        NetworkErrorType.SslException -> ConnectivityError.HostUnreachable

        NetworkErrorType.HttpException -> when (this.code) {
            401 -> NetworkError.Unauthorized
            403 -> NetworkError.InsufficientPermissions
            404 -> NetworkError.NotFound
            in 500..599 -> NetworkError.ServerError
            else -> NetworkError.Unknown(description = this.toString())
        }

        // Serialization and Unknown are mapped to a generic Unknown,
        // as they represent unexpected issues.
        NetworkErrorType.SerializationError,
        NetworkErrorType.Unknown -> NetworkError.Unknown(description = this.toString())
    }
}
