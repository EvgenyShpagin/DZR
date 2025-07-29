package com.music.dzr.library.artist.domain.model

import com.music.dzr.core.model.Image
import com.music.dzr.core.model.MusicGenre
import com.music.dzr.core.model.PopularityLevel

/**
 * Music artist with full profile information.
 */
data class Artist(
    val id: String,
    val name: String,
    val genres: List<MusicGenre>,
    val images: List<Image>,
    val externalUrl: String,
    val followersCount: Int,
    val popularity: PopularityLevel
)
