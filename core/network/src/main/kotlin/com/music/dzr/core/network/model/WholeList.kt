package com.music.dzr.core.network.model

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