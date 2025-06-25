package com.music.dzr.core.network.model.player

import kotlinx.serialization.Serializable

/**
 * Describes a track to be removed from a playlist.
 *
 * @property uri The Spotify URI of the track to remove.
 */
@Serializable
data class TrackToRemove(
    val uri: String
)