package com.music.dzr.player.domain.model

/**
 * Represents a device that can be used to play music.
 *
 * A device can be either active (currently being used for playback) or inactive
 * (available for transferring playback to).
 *
 * @property id Unique identifier of the device. May be `null` for some device types
 * @property isActive Whether this device is currently active
 * @property isRestricted Whether controlling this device is restricted
 * @property name The name of the device as it appears to the user
 * @property type The type of device
 * @property volumePercent Current volume level as a percentage (0-100). May be `null` if the device doesn't support volume control
 * @property supportsVolume Whether the device supports volume control
 *
 * @see DeviceType
 */
data class Device(
    val id: String?,
    val isActive: Boolean,
    val isRestricted: Boolean,
    val name: String,
    val type: DeviceType,
    val volumePercent: Int?,
    val supportsVolume: Boolean
)