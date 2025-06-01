package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

/**
 * A universal wrapper for all API responses.
 */
@Serializable
data class ApiResponse<T>(
    val data: T? = null,
    val error: NetworkError? = null
)