package com.music.dzr.library.track.domain.model

import com.music.dzr.core.model.Album
import com.music.dzr.core.model.ContentRestriction
import com.music.dzr.core.model.Image
import com.music.dzr.core.model.Market
import com.music.dzr.core.model.ReleaseDate
import com.music.dzr.core.model.ReleaseType
import com.music.dzr.core.model.SimplifiedArtist

/**
 * Album reference for track context.
 */
data class AlbumOnTrack(
    override val id: String,
    override val name: String,
    override val releaseType: ReleaseType,
    override val totalTracks: Int,
    override val images: List<Image>,
    override val releaseDate: ReleaseDate,
    override val availableMarkets: List<Market>,
    override val externalUrl: String,
    override val artists: List<SimplifiedArtist>,
    override val restriction: ContentRestriction?,
) : Album()