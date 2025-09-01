package com.music.dzr.player.domain.model

/**
 * Represents the target device for repository operations.
 *
 * Use [Current] to refer to the device currently in use,
 * or [Specific] to target a device by its unique id.
 */
sealed interface TargetDevice {
    data object Current : TargetDevice
    data class Specific(val id: String) : TargetDevice
}
