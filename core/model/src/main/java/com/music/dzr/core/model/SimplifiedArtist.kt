package com.music.dzr.core.model

/**
 * Artist reference used in album and track contexts.
 */
data class SimplifiedArtist(
    val id: String,
    val name: String,
    val externalUrl: String
)