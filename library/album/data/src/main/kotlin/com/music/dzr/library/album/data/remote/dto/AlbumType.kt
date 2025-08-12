package com.music.dzr.library.album.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the Spotify type of the album
 */
@Serializable
internal enum class AlbumType {

    @SerialName("album")
    Album,

    @SerialName("single")
    Single,

    @SerialName("compilation")
    Compilation
}