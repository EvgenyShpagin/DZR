package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.album.ReleaseDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


/**
 * A custom serializer for [ReleaseDate] that catches the following date formats:
 * - yyyy-MM-dd
 * - yyyy-MM
 * - yyyy
 */
internal object ReleaseDateSerializer : KSerializer<ReleaseDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "ReleaseDateSerializer",
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): ReleaseDate {
        val raw = decoder.decodeString()
        val splitValues = raw.split('-')
        val splitNumbers = splitValues.map {
            it.toIntOrNull() ?: throwDeserializationException(raw)
        }
        return ReleaseDate(
            year = splitNumbers[0],
            month = splitNumbers.getOrNull(1),
            day = splitNumbers.getOrNull(2),
        )
    }

    override fun serialize(encoder: Encoder, value: ReleaseDate) {
        val dateString = when {
            value.month == null -> "${value.year}"
            value.day == null -> "${value.year}-${value.month}"
            else -> "${value.year}-${value.month}-${value.day}"
        }
        encoder.encodeString(dateString)
    }
}
