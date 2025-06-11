package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.DeviceType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A custom serializer for [DeviceType].
 */
object DeviceTypeSerializer : KSerializer<DeviceType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "DeviceTypeSerializer",
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): DeviceType {
        return when (decoder.decodeString()) {
            "computer" -> DeviceType.Computer
            "tablet" -> DeviceType.Tablet
            "smartphone" -> DeviceType.Smartphone
            "speaker" -> DeviceType.Speaker
            "tv" -> DeviceType.Tv
            "avr" -> DeviceType.Avr
            "stb" -> DeviceType.Stb
            "audio_dongle" -> DeviceType.AudioDongle
            "game_console" -> DeviceType.GameConsole
            "cast_video" -> DeviceType.CastVideo
            "cast_audio" -> DeviceType.CastAudio
            "automobile" -> DeviceType.Automobile
            else -> DeviceType.Unknown
        }
    }

    override fun serialize(encoder: Encoder, value: DeviceType) {
        val valueAsString = when (value) {
            DeviceType.Computer -> "computer"
            DeviceType.Tablet -> "tablet"
            DeviceType.Smartphone -> "smartphone"
            DeviceType.Speaker -> "speaker"
            DeviceType.Tv -> "tv"
            DeviceType.Avr -> "avr"
            DeviceType.Stb -> "stb"
            DeviceType.AudioDongle -> "audio_dongle"
            DeviceType.GameConsole -> "game_console"
            DeviceType.CastVideo -> "cast_video"
            DeviceType.CastAudio -> "cast_audio"
            DeviceType.Automobile -> "automobile"
            DeviceType.Unknown -> "unknown"
        }
        return encoder.encodeString(valueAsString)
    }
} 