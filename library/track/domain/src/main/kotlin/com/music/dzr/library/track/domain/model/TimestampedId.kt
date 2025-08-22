package com.music.dzr.library.track.domain.model

import kotlin.time.Instant

/**
 * Track identifier paired with the timestamp when it was added to the user's library.
 */
data class TimestampedId(
    val id: String,
    val addedAt: Instant,
)
