package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.AlbumGroup
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A custom serializer for [AlbumGroup].
 * Possible values: "album", "single", "compilation"
 */
object AlbumGroupSerializer : KSerializer<AlbumGroup> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "AlbumGroupSerializer",
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): AlbumGroup {
        val raw = decoder.decodeString()
        return when (raw) {
            "album" -> AlbumGroup.Album
            "single" -> AlbumGroup.Single
            "appears_on" -> AlbumGroup.AppearsOn
            else -> AlbumGroup.Compilation
        }
    }

    override fun serialize(encoder: Encoder, value: AlbumGroup) {
        val valueAsString = when (value) {
            AlbumGroup.Album -> "album"
            AlbumGroup.Single -> "single"
            AlbumGroup.Compilation -> "compilation"
            AlbumGroup.AppearsOn -> "appears_on"
        }
        return encoder.encodeString(valueAsString)
    }
}