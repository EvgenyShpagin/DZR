package com.music.dzr.core.model.artist

import com.music.dzr.core.model.shared.Image
import com.music.dzr.core.model.shared.MusicGenre

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
    val popularityScore: Int
)
