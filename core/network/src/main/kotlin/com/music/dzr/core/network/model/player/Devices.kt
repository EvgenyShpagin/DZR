package com.music.dzr.core.network.model.player

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