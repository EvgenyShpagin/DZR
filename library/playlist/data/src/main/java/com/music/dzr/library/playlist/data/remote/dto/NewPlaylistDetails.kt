package com.music.dzr.library.playlist.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Request body for creating a playlist.
 *
 * @property name The name for the new playlist, for example "Your Coolest Playlist".
 * @property public Defaults to `true`. If `true` the playlist will be public, if `false` it will be private.
 * @property collaborative Defaults to `false`. If `true` the playlist will be collaborative.
 * @property description value for playlist description as displayed in Spotify Clients and in the Web API.
 */
@Serializable
data class NewPlaylistDetails(
    val name: String,
    val public: Boolean? = null,
    val collaborative: Boolean? = null,
    val description: String? = null
)