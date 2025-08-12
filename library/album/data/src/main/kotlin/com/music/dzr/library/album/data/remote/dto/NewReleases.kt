package com.music.dzr.library.album.data.remote.dto

import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.SimplifiedAlbum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Paging object for new releases.
 */
@Serializable
data class NewReleases(
    @SerialName("albums")
    val list: PaginatedList<SimplifiedAlbum>
)