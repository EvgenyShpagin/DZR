package com.music.dzr.core.model

/**
 * Artist reference used in album and track contexts.
 */
data class SimplifiedArtist(
    override val id: String,
    override val name: String,
    override val externalUrl: String
) : Artist()