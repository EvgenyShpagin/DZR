package com.music.dzr.library.artist.domain.model

import com.music.dzr.core.model.Artist
import com.music.dzr.core.model.Image
import com.music.dzr.core.model.MusicGenre
import com.music.dzr.core.model.PopularityLevel

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
