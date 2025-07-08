package com.music.dzr.core.model.album

import com.music.dzr.core.model.artist.ArtistSimplified
import com.music.dzr.core.model.shared.ContentRestriction
import com.music.dzr.core.model.shared.Copyright
import com.music.dzr.core.model.shared.Image
import com.music.dzr.core.model.shared.Market
import com.music.dzr.core.model.track.PopularityLevel
import com.music.dzr.core.model.track.Track

/**
 * Music album with metadata and optional detailed information.
 */
data class Album(
    val id: String,
    val name: String,
    val releaseType: ReleaseType,
    val artists: List<ArtistSimplified>,
    val images: List<Image>,
    val releaseDate: ReleaseDate,
    val totalTracks: Int,
    val availableMarkets: List<Market>,
    val uri: String,
    val restriction: ContentRestriction? = null,
    val tracks: List<Track>? = null,
    val copyrights: List<Copyright>? = null,
    val genres: List<String>? = null,
    val label: String? = null,
    val popularity: PopularityLevel? = null
)
