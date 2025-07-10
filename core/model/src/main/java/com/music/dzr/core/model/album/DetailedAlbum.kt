package com.music.dzr.core.model.album

import com.music.dzr.core.model.artist.SimplifiedArtist
import com.music.dzr.core.model.shared.ContentRestriction
import com.music.dzr.core.model.shared.Copyright
import com.music.dzr.core.model.shared.ExternalIdentifiers
import com.music.dzr.core.model.shared.Image
import com.music.dzr.core.model.shared.Market
import com.music.dzr.core.model.shared.MusicGenre
import com.music.dzr.core.model.shared.PopularityLevel
import com.music.dzr.core.model.track.TrackOnAlbum

/**
 * Album with full information including tracks.
 */
data class DetailedAlbum(
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
    val tracks: List<TrackOnAlbum>,
    val copyrights: List<Copyright>,
    val externalIds: ExternalIdentifiers,
    val genres: List<MusicGenre>,
    val label: String,
    val popularity: PopularityLevel
) : Album()