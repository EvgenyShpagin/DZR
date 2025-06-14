package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

/**
 * Request body for changing a playlist's details.
 *
 * @property name The new name for the playlist.
 * @property public If `true` the playlist will be public, if `false` it will be private.
 * @property collaborative If `true`, the playlist will become collaborative and other users will be able to modify the playlist in their Spotify client.
 * @property description Value for playlist description as displayed in Spotify Clients and in the Web API.
 */
@Serializable
data class ChangePlaylistDetailsRequest(
    val name: String? = null,
    val public: Boolean? = null,
    val collaborative: Boolean? = null,
    val description: String? = null
)