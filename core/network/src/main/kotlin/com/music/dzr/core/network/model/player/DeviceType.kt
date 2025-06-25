package com.music.dzr.core.network.model.player

import com.music.dzr.core.network.serialization.DeviceTypeSerializer
import kotlinx.serialization.Serializable

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