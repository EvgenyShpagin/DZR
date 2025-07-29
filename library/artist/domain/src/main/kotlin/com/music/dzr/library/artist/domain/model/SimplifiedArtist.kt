package com.music.dzr.library.artist.domain.model

/**
 * Artist reference used in album and track contexts.
 */
data class SimplifiedArtist(
    val id: String,
    val name: String,
    val externalUrl: String
)
