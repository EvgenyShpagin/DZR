package com.music.dzr.core.network.model.player

import kotlinx.serialization.Serializable

/**
 * A container for a list of [Device] objects.
 *
 * @property devices A list of devices.
 */
@Serializable
data class DevicesContainer(
    val devices: List<Device>
) 