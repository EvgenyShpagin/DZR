package com.music.dzr.player.domain.model

import com.music.dzr.core.pagination.CursorPageable
import kotlin.time.Instant

/**
 * Request model for recently played endpoint.
 */
sealed class RecentlyPlayedFilter {
    abstract val limit: Int

    /**
     * Get recent tracks without time constraints.
     */
    data class Recent(
        override val limit: Int
    ) : RecentlyPlayedFilter()

    /**
     * Get tracks played since a specific time.
     */
    data class Since(
        val time: Instant,
        override val limit: Int = DEFAULT_LIMIT
    ) : RecentlyPlayedFilter()

    /**
     * Get tracks played until a specific time.
     */
    data class Until(
        val time: Instant,
        override val limit: Int = DEFAULT_LIMIT
    ) : RecentlyPlayedFilter()

    companion object {
        private val DEFAULT_LIMIT = CursorPageable.Default.limit
        val Default = Recent(limit = DEFAULT_LIMIT)
    }
}
