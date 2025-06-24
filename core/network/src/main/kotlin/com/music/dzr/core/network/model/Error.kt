package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * A unified model for representing errors across the application.
 *
 * This class encapsulates various types of errors, including standard API errors,
 * OAuth 2.0 authentication errors, and network-related issues like connection problems.
 *
 * @property type The high-level category of the error, defined by [NetworkErrorType].
 * @property message A human-readable description of the error. For OAuth errors,
 *           this corresponds to the `error_description` field.
 * @property code For HTTP errors, this is the HTTP status code. For other errors,
 *           it may hold a value from [NetworkErrorType].
 * @property reason An optional machine-readable error code. For standard API errors,
 *          this could be a specific reason like "PLAYER_ERROR". For OAuth errors,
 *          it corresponds to the `error` field (e.g., "invalid_grant").
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

    /** Error occurs when it is not possible to convert json objects to kotlin objects or vice versa */
    SerializationError,

    /** Catch-all for any other unforeseen network or runtime errors */
    Unknown
}
