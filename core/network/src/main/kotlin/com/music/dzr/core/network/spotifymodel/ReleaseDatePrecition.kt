package com.music.dzr.core.network.spotifymodel

import com.music.dzr.core.network.util.ReleaseDatePrecisionSerializer
import com.music.dzr.core.network.util.ReleaseDateSerializer
import kotlinx.serialization.Serializable

/**
 * The date the object was first released.
 */
@Serializable(with = ReleaseDateSerializer::class)
data class ReleaseDate(
    val year: Int,
    val month: Int?,
    val day: Int?
)

/**
 * The precision with which [ReleaseDate] value is known.
 */
@Serializable(with = ReleaseDatePrecisionSerializer::class)
enum class ReleaseDatePrecision {
    YEAR,
    MONTH,
    DAY
}
