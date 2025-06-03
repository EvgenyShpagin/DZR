package com.music.dzr.core.network.util

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A custom serializer for [LocalDateTime] that catches the format with a space between
 * the date and time and converts it to an ISO string with a "T".
 */
object LocalDateTimeWithSpaceSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "LocalDateTimeWithSpaceSerializer",
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val raw = decoder.decodeString()
        val iso = raw.replaceFirst(' ', 'T')
        return LocalDateTime.parse(iso)
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val withSpace = value.toString().replaceFirst('T', ' ')
        encoder.encodeString(withSpace)
    }
}

/**
 * A custom serializer for [LocalDateTime]? that catches "0000-00-00T00:00:00" date and
 * the format with a space between the date and time and converts it to an ISO string with a "T".
 *
 * "0000-00-00T00:00:00" date is usually returned when retrieving a chart playlist
 */
object ApiLenientLocalDateTimeSerializer : KSerializer<LocalDateTime?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "ApiLenientLocalDateTimeSerializer",
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): LocalDateTime? {
        val raw = decoder.decodeString()
        return if (raw == "0000-00-00 00:00:00") {
            null
        } else {
            val iso = raw.replaceFirst(' ', 'T')
            LocalDateTime.parse(iso)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: LocalDateTime?) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            val withSpace = value.toString().replaceFirst('T', ' ')
            encoder.encodeString(withSpace)
        }
    }
}
