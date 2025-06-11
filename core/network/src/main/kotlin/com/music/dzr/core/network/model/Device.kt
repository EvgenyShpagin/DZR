package com.music.dzr.core.network.model

import com.music.dzr.core.network.util.DeviceTypeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a device that can be used to play Spotify content.
 *
 * @property id The device ID. This may be `null`.
 * @property isActive If this device is the currently active device.
 * @property isPrivateSession If this device is currently in a private session.
 * @property isRestricted Whether controlling this device is restricted.
 * @property name The name of the device.
 * @property type Device type.
 * @property volumePercent The current volume in percent. This may be `null`.
 * @property supportsVolume If this device can be used to set the volume.
 */
@Serializable
data class Device(
    val id: String?,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("is_private_session")
    val isPrivateSession: Boolean,
    @SerialName("is_restricted")
    val isRestricted: Boolean,
    val name: String,
    val type: DeviceType,
    @SerialName("volume_percent")
    val volumePercent: Int?,
    @SerialName("supports_volume")
    val supportsVolume: Boolean
)

/**
 * The type of device.
 *
 * @property Computer Computer
 * @property Tablet Tablet
 * @property Smartphone Smartphone
 * @property Speaker Speaker
 * @property Tv TV
 * @property Avr Audio/Visual Receiver
 * @property Stb Set-Top Box
 * @property AudioDongle Audio Dongle
 * @property GameConsole Game Console
 * @property CastVideo Cast Video
 * @property CastAudio Cast Audio
 * @property Automobile Automobile
 * @property Unknown Unknown
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