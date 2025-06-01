package com.music.dzr.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Generic response class for Deezer API.
 * Contains only a list of items.
 *
 * @param T The type of items contained in the response.
 */
@Serializable
data class WholeList<T>(
    val data: List<T>
)

/**
 * Generic pagination response class for Deezer API.
 * Contains a list of items, total count, and a link to the next page if available.
 *
 * @param T The type of items contained in the paginated response.
 */
@Serializable
data class PaginatedList<T>(
    val data: List<T>,
    val total: Int? = null,
    @SerialName("next") val nextResultsUrl: String? = null,
    @SerialName("prev") val prevResultsUrl: String? = null
)

/**
 * Represents an error returned by the API.
 */
@Serializable
data class ApiError(
    val type: NetworkErrorType,
    val message: String,
    val code: Int
)

/**
 * Represents the various types of errors that can occur when interacting
 * with the API or performing network operations.
 */
enum class NetworkErrorType {

    // Deezer API errors:

    /** Used when the request fails due to exceeded quota, item limit reached, or server overload. */
    Exception,

    /** Error related to OAuth authentication or token issues */
    OAuthException,

    /** Error due to invalid or malformed request parameters */
    ParameterException,

    /** Error when a required parameter is missing from the request */
    MissingParameterException,

    /** Error when the query format or syntax is invalid */
    InvalidQueryException,

    /** Error related to data lack on the server */
    DataException,

    /** Error when changing an individual account is not permitted */
    IndividualAccountChangedNotAllowedException,

    // Network errors:

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

/**
 * A universal wrapper for all API responses.
 */
@Serializable
data class ApiResponse<T>(
    val data: T? = null,
    val error: ApiError? = null
)