package com.music.dzr.core.network.model.error

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