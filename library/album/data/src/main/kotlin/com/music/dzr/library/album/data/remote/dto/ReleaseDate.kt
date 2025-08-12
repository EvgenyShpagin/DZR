package com.music.dzr.library.album.data.remote.dto

import com.music.dzr.core.network.serialization.throwDeserializationException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * The date the object was first released.
 */
@Serializable(with = ReleaseDateSerializer::class)
internal data class ReleaseDate(
    val year: Int,
    val month: Int?,
    val day: Int?
)

/**
 * A custom serializer for [ReleaseDate] that catches the following date formats:
 * - yyyy-MM-dd
 * - yyyy-MM
 * - yyyy
 */
private object ReleaseDateSerializer : KSerializer<ReleaseDate> {
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