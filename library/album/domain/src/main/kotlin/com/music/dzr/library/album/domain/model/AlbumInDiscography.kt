package com.music.dzr.library.album.domain.model

import com.music.dzr.core.model.ContentRestriction
import com.music.dzr.core.model.Image
import com.music.dzr.core.model.Market
import com.music.dzr.library.artist.domain.model.SimplifiedArtist

/**
 * Album in artist's discography.
 */
data class AlbumInDiscography(
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
    val justAppearsOn: Boolean
) : Album()