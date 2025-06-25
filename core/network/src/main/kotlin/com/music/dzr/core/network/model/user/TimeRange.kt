package com.music.dzr.core.network.model.user

import com.music.dzr.core.network.retrofit.UrlParameter
import com.music.dzr.core.network.util.getSerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents range over what time frame the affinities are computed.
 * Used for getting top user tracks/artists.
 */
@Serializable
enum class TimeRange : UrlParameter {
    /**
     * Calculated from ~1 year of data and including all new data as it becomes available.
     */
    @SerialName("long_term")
    LongTerm,

    /**
     * Approximately last 6 months.
     */
    @SerialName("medium_term")
    MediumTerm,

    /**
     * Approximately last 4 weeks.
     */
    @SerialName("short_term")
    ShortTerm;

    override val urlValue get() = getSerializedName()
}