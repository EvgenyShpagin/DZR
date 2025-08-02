package com.music.dzr.library.player.data.remote.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * The type of device.
 */
@Serializable(with = DeviceTypeSerializer::class)
enum class DeviceType {
    Computer,
    Tablet,
    Smartphone,
    Speaker,
    Tv,
    Avr,
    Stb,
    AudioDongle,
    GameConsole,
    CastVideo,
    CastAudio,
    Automobile,
    Unknown
}

/**
 * A custom serializer for [DeviceType] that gracefully handles unknown values.
 * If an unknown device type string is received from the API, it defaults to [DeviceType.Unknown].
 */
internal object DeviceTypeSerializer : KSerializer<DeviceType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "DeviceTypeSerializer",
        PrimitiveKind.STRING
    )

    private val stringToEnumMap = mapOf(
        "computer" to DeviceType.Computer,
        "tablet" to DeviceType.Tablet,
        "smartphone" to DeviceType.Smartphone,
        "speaker" to DeviceType.Speaker,
        "tv" to DeviceType.Tv,
        "avr" to DeviceType.Avr,
        "stb" to DeviceType.Stb,
        "audio_dongle" to DeviceType.AudioDongle,
        "game_console" to DeviceType.GameConsole,
        "cast_video" to DeviceType.CastVideo,
        "cast_audio" to DeviceType.CastAudio,
        "automobile" to DeviceType.Automobile,
        "unknown" to DeviceType.Unknown
    )
    private val enumToStringMap = stringToEnumMap.entries.associate { (k, v) -> v to k }

    override fun deserialize(decoder: Decoder): DeviceType {
        val value = decoder.decodeString()
        return stringToEnumMap[value] ?: DeviceType.Unknown
    }

    override fun serialize(encoder: Encoder, value: DeviceType) {
        encoder.encodeString(enumToStringMap.getValue(value))
    }
}