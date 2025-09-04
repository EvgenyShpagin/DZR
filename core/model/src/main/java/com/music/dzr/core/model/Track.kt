package com.music.dzr.core.model

import kotlin.time.Duration

/**
 * Represents common track information across different contexts.
 *
 * Note that [externalUrl] is not available when the track is local.
 */
abstract class Track : AudioContent {
    abstract override val id: String
    abstract override val availability: TrackAvailability
    abstract val name: String
    abstract val artists: List<SimplifiedArtist>
    abstract val duration: Duration
    abstract val trackNumber: Int
    abstract val discNumber: Int
    abstract val isExplicit: Boolean
    abstract val externalUrl: String?
}