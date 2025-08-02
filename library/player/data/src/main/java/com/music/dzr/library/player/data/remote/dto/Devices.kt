package com.music.dzr.library.player.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A container for a list of [Device] objects.
 *
 * @property list A list of devices.
 */
@Serializable
data class Devices(
    @SerialName("devices")
    val list: List<Device>
) 