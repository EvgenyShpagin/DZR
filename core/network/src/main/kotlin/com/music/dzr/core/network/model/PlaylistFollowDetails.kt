package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

/**
 * Request body for following a playlist.
 *
 * @property public If `true` the playlist will be included in user's public playlists, if `false` if false it will remain private.
 */
@Serializable
data class PlaylistFollowDetails(
    val public: Boolean = true,
)