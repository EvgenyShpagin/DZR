package com.music.dzr.library.artist.data.remote.dto

import com.music.dzr.core.network.dto.Artist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple artists
 */
@Serializable
internal data class Artists(
    @SerialName("artists")
    val list: List<Artist>
) 