package com.music.dzr.core.network.model

import com.music.dzr.core.network.util.ReleaseDateSerializer
import kotlinx.serialization.SerialName
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
@Serializable
enum class ReleaseDatePrecision {
    @SerialName("year")
    YEAR,

    @SerialName("month")
    MONTH,

    @SerialName("day")
    DAY
}
