package com.music.dzr.core.network.model.user

import com.music.dzr.core.network.model.artist.Artist
import com.music.dzr.core.network.model.shared.CursorPaginatedList
import kotlinx.serialization.Serializable

/**
 * A container for a cursor-paginated list of followed artists.
 *
 * @property artists The paginated list of followed artists.
 */
@Serializable
data class FollowedArtistsContainer(
    val artists: CursorPaginatedList<Artist>,
)