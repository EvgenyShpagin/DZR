package com.music.dzr.core.network.dto

import com.music.dzr.core.network.dto.error.NetworkError
import kotlinx.serialization.Serializable

/**
 * A universal wrapper for all API responses.
 */
@Serializable
data class NetworkResponse<T>(
    val data: T? = null,
    val error: NetworkError? = null
)