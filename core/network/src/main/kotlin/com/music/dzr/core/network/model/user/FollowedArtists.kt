package com.music.dzr.core.network.model.user

import com.music.dzr.core.network.model.artist.Artist
import com.music.dzr.core.network.model.shared.CursorPaginatedList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A container for a cursor-paginated list of followed artists.
 *
 * @property items The paginated list of followed artists.
 */
@Serializable
data class FollowedArtists(
    @SerialName("artists")
    val items: CursorPaginatedList<Artist>,
)