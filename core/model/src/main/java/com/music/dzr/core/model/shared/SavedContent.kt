package com.music.dzr.core.model.shared

import kotlin.time.Instant

/**
 * User's saved music content with timestamp.
 */
data class SavedContent<T>(
    val addedAt: Instant,
    val music: T
)
