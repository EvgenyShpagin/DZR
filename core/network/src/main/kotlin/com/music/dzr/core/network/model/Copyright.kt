package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

/**
 * A copyright statement for an album.
 */
@Serializable
data class Copyright(
    val text: String,
    val type: String
)