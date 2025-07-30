package com.music.dzr.library.track.domain.model

import com.music.dzr.core.model.AudioContent
import com.music.dzr.core.model.ContentRestriction
import com.music.dzr.core.model.SimplifiedArtist
import kotlin.time.Duration

/**
 * Represents common track information across different contexts.
 */
sealed class Track : AudioContent {
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
