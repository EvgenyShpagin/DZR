package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.CurrentlyPlayingType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A custom serializer for [CurrentlyPlayingType].
 * Possible values: "track", "episode", "ad", "unknown"
 */
object CurrentlyPlayingTypeSerializer : KSerializer<CurrentlyPlayingType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "CurrentlyPlayingTypeSerializer",
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): CurrentlyPlayingType {
        val raw = decoder.decodeString()
        return when (raw) {
            "track" -> CurrentlyPlayingType.Track
            "episode" -> CurrentlyPlayingType.Episode
            "ad" -> CurrentlyPlayingType.Ad
            else -> CurrentlyPlayingType.Unknown
        }
    }

    override fun serialize(encoder: Encoder, value: CurrentlyPlayingType) {
        val valueAsString = when (value) {
            CurrentlyPlayingType.Track -> "track"
            CurrentlyPlayingType.Episode -> "episode"
            CurrentlyPlayingType.Ad -> "ad"
            CurrentlyPlayingType.Unknown -> "unknown"
        }
        return encoder.encodeString(valueAsString)
    }
}