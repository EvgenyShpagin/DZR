package com.music.dzr.core.network.model.album

import com.music.dzr.core.network.model.shared.PaginatedList
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