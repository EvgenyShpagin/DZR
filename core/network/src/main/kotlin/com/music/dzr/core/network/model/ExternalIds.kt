package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

/**
 * External identifiers (ISRC, EAN, UPC) for an album.
 */
@Serializable
data class ExternalIds(
    val isrc: String? = null,
    val ean: String? = null,
    val upc: String? = null
)