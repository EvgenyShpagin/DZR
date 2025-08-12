package com.music.dzr.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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