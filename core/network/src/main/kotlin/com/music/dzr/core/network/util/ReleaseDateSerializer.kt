package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.ReleaseDate
import com.music.dzr.core.network.model.ReleaseDatePrecision
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


/**
 * A custom serializer for [ReleaseDate] that catches the following date formats:
 * - yyyy-mm-dd
 * - yyyy-mm
 * - yyyy
 */
object ReleaseDateSerializer : KSerializer<ReleaseDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "ReleaseDateSerializer",
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): ReleaseDate {
        val raw = decoder.decodeString()
        val splitValues = raw.split('-')
        val splitNumbers = splitValues.map { it.toInt() }
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

/**
 * A custom serializer for [ReleaseDatePrecision]
 */
object ReleaseDatePrecisionSerializer : KSerializer<ReleaseDatePrecision> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "ReleaseDatePrecisionSerializer",
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): ReleaseDatePrecision {
        val raw = decoder.decodeString()
        return when (raw) {
            "day" -> ReleaseDatePrecision.DAY
            "month" -> ReleaseDatePrecision.MONTH
            else -> ReleaseDatePrecision.YEAR
        }
    }

    override fun serialize(encoder: Encoder, value: ReleaseDatePrecision) {
        val raw = when (value) {
            ReleaseDatePrecision.YEAR -> "year"
            ReleaseDatePrecision.MONTH -> "month"
            ReleaseDatePrecision.DAY -> "day"
        }
        encoder.encodeString(raw)
    }
}
