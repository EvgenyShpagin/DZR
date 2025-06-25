package com.music.dzr.core.network.model.artist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple artists
 */
@Serializable
data class Artists(
    @SerialName("artists") val list: List<Artist>
) 