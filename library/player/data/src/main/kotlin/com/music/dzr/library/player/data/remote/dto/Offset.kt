package com.music.dzr.library.player.data.remote.dto

import com.music.dzr.core.network.serialization.throwDeserializationException
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

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

/**
 * A custom serializer for [Offset].
 */
private object OffsetSerializer : JsonContentPolymorphicSerializer<Offset>(Offset::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Offset> {
        val jsonObject = element.jsonObject
        return when {
            "position" in jsonObject -> Offset.ByPosition.serializer()
            "uri" in jsonObject -> Offset.ByUri.serializer()
            else -> throwDeserializationException(jsonObject.toString())
        }
    }
}