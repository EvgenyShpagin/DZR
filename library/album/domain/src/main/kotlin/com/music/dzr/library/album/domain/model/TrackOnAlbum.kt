package com.music.dzr.library.album.domain.model

import com.music.dzr.core.model.ContentRestriction
import com.music.dzr.core.model.SimplifiedArtist
import com.music.dzr.core.model.Track
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
    override val externalUrl: String,
    override val restriction: ContentRestriction?
) : Track()