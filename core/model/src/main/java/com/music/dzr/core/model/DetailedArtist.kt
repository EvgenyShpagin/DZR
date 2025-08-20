package com.music.dzr.core.model

/**
 * Music artist with full profile information.
 */
data class DetailedArtist(
    override val id: String,
    override val name: String,
    override val externalUrl: String,
    val genres: List<MusicGenre>,
    val images: List<Image>,
    val followersCount: Int,
    val popularity: PopularityLevel
) : Artist()
