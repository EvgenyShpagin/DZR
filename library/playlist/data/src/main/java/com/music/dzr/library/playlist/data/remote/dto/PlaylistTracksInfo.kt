package com.music.dzr.library.playlist.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Represents a reference to a list of tracks in a playlist, without the track items themselves.
 *
 * @property href A link to the Web API endpoint providing full details of the tracks.
 * @property total The total number of tracks in the playlist.
 */
@Serializable
data class PlaylistTracksInfo(
    val href: String,
    val total: Int,
)