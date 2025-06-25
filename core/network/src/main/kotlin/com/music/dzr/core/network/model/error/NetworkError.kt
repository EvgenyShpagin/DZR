package com.music.dzr.core.network.model.error

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