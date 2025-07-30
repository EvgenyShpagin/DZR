package com.music.dzr.core.model

import kotlin.time.Duration

/**
 * Represents common track information across different contexts.
 */
abstract class Track : AudioContent {
    abstract override val id: String
    abstract val name: String
    abstract val artists: List<SimplifiedArtist>
    abstract val duration: Duration
    abstract val trackNumber: Int
    abstract val discNumber: Int
    abstract val isExplicit: Boolean
    abstract val isPlayable: Boolean
    abstract val isLocal: Boolean
    abstract val externalUrl: String
    abstract val restriction: ContentRestriction?
}