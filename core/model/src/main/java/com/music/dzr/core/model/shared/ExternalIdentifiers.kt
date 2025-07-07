package com.music.dzr.core.model.shared

/**
 * External identifiers for music content.
 */
data class ExternalIdentifiers(
    val isrc: String? = null,  // International Standard Recording Code
    val ean: String? = null,   // European Article Number
    val upc: String? = null    // Universal Product Code
)
