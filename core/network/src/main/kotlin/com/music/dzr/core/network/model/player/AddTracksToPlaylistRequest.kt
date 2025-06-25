package com.music.dzr.core.network.model.player

import kotlinx.serialization.Serializable

/**
 * Request body for adding tracks to a playlist.
 *
 * @property uris A JSON array of the Spotify URIs to add.
 * @property position The position to insert the items, a zero-based index.
 */
@Serializable
data class AddTracksToPlaylistRequest(
    val uris: List<String>,
    val position: Int? = null,
)