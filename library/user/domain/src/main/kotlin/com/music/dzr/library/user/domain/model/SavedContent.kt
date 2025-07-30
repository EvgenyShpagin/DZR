package com.music.dzr.library.user.domain.model

import kotlin.time.Instant

/**
 * User's saved music content with timestamp.
 */
data class SavedContent<T>(
    val addedAt: Instant,
    val music: T
)