package com.music.dzr.core.network.model.track

import kotlinx.serialization.Serializable

/**
 * Wrapper for getting track collection
 */
@Serializable
data class TracksContainer(
    val tracks: List<Track>
) 