package com.music.dzr.core.model

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
