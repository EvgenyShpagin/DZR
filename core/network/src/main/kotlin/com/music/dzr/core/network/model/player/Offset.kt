package com.music.dzr.core.network.model.player

import com.music.dzr.core.network.util.OffsetSerializer
import kotlinx.serialization.Serializable

/**
 * A sealed interface representing where in the context playback should start.
 * Used within [PlaybackOptions].
 */
@Serializable(with = OffsetSerializer::class)
sealed interface Offset {
    /**
     * Specifies the track to start playback from by its position in the context.
     * @property position The zero-based index of the track.
     */
    @Serializable
    data class ByPosition(val position: Int) : Offset

    /**
     * Specifies the track to start playback from by its URI.
     * @property uri The URI of the track.
     */
    @Serializable
    data class ByUri(val uri: String) : Offset
}