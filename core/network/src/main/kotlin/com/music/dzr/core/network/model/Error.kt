package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents an error returned by the API.
 */
@Serializable
data class NetworkError(
    val type: NetworkErrorType,
    val message: String,
    @SerialName("status") val code: Int,
    val reason: String? = null
)

/**
 * Represents the various types of errors that can occur when interacting
 * with the API or performing network operations.
 */
enum class NetworkErrorType {

    /** Error when DNS resolution fails (e.g., UnknownHostException) */
    UnknownHost,

    /** General connection failure (e.g., no network, generic IOException) */
    SomeConnectionError,

    /** Error when the host is unreachable (e.g., ConnectException) */
    UnreachableHost,

    /** Error for any HTTP response code in the 4xx or 5xx range */
    HttpException,

    /** Error related to SSL/TLS handshake failures or certificate issues */
    SslException,

    /** Error when a connection or request read/write times out */
    Timeout,

    /** Catch-all for any other unforeseen network or runtime errors */
    Unknown
}
