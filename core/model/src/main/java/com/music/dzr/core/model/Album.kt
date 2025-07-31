package com.music.dzr.core.model

/**
 * Album representation for different contexts.
 */
abstract class Album {
    abstract val id: String
    abstract val name: String
    abstract val releaseType: ReleaseType
    abstract val totalTracks: Int
    abstract val images: List<Image>
    abstract val releaseDate: ReleaseDate
    abstract val availableMarkets: List<Market>
    abstract val externalUrl: String
    abstract val artists: List<SimplifiedArtist>
    abstract val restriction: ContentRestriction?
}