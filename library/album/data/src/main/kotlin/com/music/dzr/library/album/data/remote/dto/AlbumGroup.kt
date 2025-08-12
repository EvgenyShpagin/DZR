package com.music.dzr.library.album.data.remote.dto

import com.music.dzr.core.network.retrofit.UrlParameter
import com.music.dzr.core.network.serialization.getSerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This describes the relationship between the artist and the album.
 */
@Serializable
internal enum class AlbumGroup : UrlParameter {
    @SerialName("album")
    Album,

    @SerialName("single")
    Single,

    @SerialName("compilation")
    Compilation,

    @SerialName("appears_on")
    AppearsOn;

    override val urlValue get() = getSerializedName()
}