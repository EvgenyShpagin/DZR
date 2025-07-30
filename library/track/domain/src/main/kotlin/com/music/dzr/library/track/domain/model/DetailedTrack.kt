package com.music.dzr.library.track.domain.model

import com.music.dzr.core.model.ContentRestriction
import com.music.dzr.core.model.ExternalIdentifiers
import com.music.dzr.core.model.PopularityLevel
import com.music.dzr.core.model.SimplifiedArtist
import kotlin.time.Duration

/**
 * Represents a full track with detailed information, including its album.
 */
data class DetailedTrack(
    override val id: String,
    override val name: String,
    override val artists: List<SimplifiedArtist>,
    override val duration: Duration,
    override val trackNumber: Int,
    override val discNumber: Int,
    override val isExplicit: Boolean,
    override val isPlayable: Boolean,
    override val isLocal: Boolean,
    override val externalUrl: String,
    override val restriction: ContentRestriction?,
    val album: AlbumOnTrack,
    val popularity: PopularityLevel,
    val externalIds: ExternalIdentifiers
) : Track()
