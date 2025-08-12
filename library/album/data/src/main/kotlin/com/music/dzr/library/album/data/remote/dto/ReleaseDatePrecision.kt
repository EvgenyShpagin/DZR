package com.music.dzr.library.album.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The precision with which [ReleaseDate] value is known.
 */
@Serializable
internal enum class ReleaseDatePrecision {
    @SerialName("year")
    YEAR,

    @SerialName("month")
    MONTH,

    @SerialName("day")
    DAY
}
