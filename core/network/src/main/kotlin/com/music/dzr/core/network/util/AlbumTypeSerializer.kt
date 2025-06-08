package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.AlbumType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A custom serializer for [AlbumType].
 * Possible values: "album", "single", "compilation"
 */
object AlbumTypeSerializer : KSerializer<AlbumType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "AlbumTypeSerializer",
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): AlbumType {
        val raw = decoder.decodeString()
        return when (raw) {
            "album" -> AlbumType.Album
            "single" -> AlbumType.Single
            else -> AlbumType.Compilation
        }
    }

    override fun serialize(encoder: Encoder, value: AlbumType) {
        val valueAsString = when (value) {
            AlbumType.Album -> "album"
            AlbumType.Single -> "single"
            AlbumType.Compilation -> "compilation"
        }
        return encoder.encodeString(valueAsString)
    }
}