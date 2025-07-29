package com.music.dzr.library.album.domain.model

import com.music.dzr.core.model.artist.SimplifiedArtist
import com.music.dzr.core.model.shared.ContentRestriction
import com.music.dzr.core.model.shared.Image
import com.music.dzr.core.model.shared.Market

/**
 * Album representations for different contexts.
 */
sealed class Album {
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
