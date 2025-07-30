package com.music.dzr.library.user.domain.model

/**
 * Represents a time period for calculating user preferences and statistics.
 *
 * Used to determine over what time frame user affinities (top tracks, artists, etc.)
 * are computed. The period is specified in months, providing full flexibility for
 * different analysis timeframes while maintaining type safety.
 *
 * Examples: `TimeRange(1)` for recent preferences, `TimeRange(6)` for medium-term,
 * `TimeRange(12)` for long-term analysis.
 */
@JvmInline
value class TimeRange(val months: Int) {
    
    init {
        require(months > 0) { "Time range must be positive" }
    }
}
