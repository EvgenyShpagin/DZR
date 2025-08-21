package com.music.dzr.core.model

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