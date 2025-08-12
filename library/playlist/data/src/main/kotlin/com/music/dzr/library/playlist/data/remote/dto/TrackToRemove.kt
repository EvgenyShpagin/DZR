package com.music.dzr.library.playlist.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Describes a track to be removed from a playlist.
 *
 * @property uri The Spotify URI of the track to remove.
 */
@Serializable
internal data class TrackToRemove(
    val uri: String
)