package com.music.dzr.core.network.model

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