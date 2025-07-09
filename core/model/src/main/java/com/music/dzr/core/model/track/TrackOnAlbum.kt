package com.music.dzr.core.model.track

import com.music.dzr.core.model.artist.SimplifiedArtist
import kotlin.time.Duration

/**
 * A simplified track representation, typically used in lists like an album's track list.
 */
data class TrackOnAlbum(
    override val id: String,
    override val name: String,
    override val artists: List<SimplifiedArtist>,
    override val duration: Duration,
    override val trackNumber: Int,
    override val discNumber: Int,
    override val isExplicit: Boolean,
    override val isPlayable: Boolean,
    override val isLocal: Boolean,
    override val externalUrl: String
) : Track()
