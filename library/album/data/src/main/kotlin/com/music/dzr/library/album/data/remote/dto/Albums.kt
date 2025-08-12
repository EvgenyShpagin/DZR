package com.music.dzr.library.album.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple albums.
 */
@Serializable
internal data class Albums(
    @SerialName("albums")
    val list: List<Album>
)