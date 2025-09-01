package com.music.dzr.library.player.data.mapper

import com.music.dzr.library.player.data.remote.dto.Device as NetworkDevice
import com.music.dzr.library.player.data.remote.dto.DeviceType as NetworkDeviceType
import com.music.dzr.library.player.data.remote.dto.Devices as NetworkDevices
import com.music.dzr.player.domain.model.Device as DomainDevice
import com.music.dzr.player.domain.model.DeviceType as DomainDeviceType
import com.music.dzr.player.domain.model.TargetDevice as NetworkTargetDevice

internal fun NetworkDevice.toDomain(): DomainDevice {
    return DomainDevice(
        id = id,
        isActive = isActive,
        isRestricted = isRestricted,
        name = name,
        type = type.toDomain(),
        volumePercent = volumePercent,
        supportsVolume = supportsVolume
    )
}

internal fun NetworkDevices.toDomain(): List<DomainDevice> {
    return list.map(NetworkDevice::toDomain)
}

private fun NetworkDeviceType.toDomain(): DomainDeviceType {
    return when (this) {
        NetworkDeviceType.Avr -> DomainDeviceType.AudioVideoReceiver
        NetworkDeviceType.Stb -> DomainDeviceType.SetTopBox
        NetworkDeviceType.Computer -> DomainDeviceType.Computer
        NetworkDeviceType.Tablet -> DomainDeviceType.Tablet
        NetworkDeviceType.Smartphone -> DomainDeviceType.Smartphone
        NetworkDeviceType.Speaker -> DomainDeviceType.Speaker
        NetworkDeviceType.Tv -> DomainDeviceType.Tv
        NetworkDeviceType.AudioDongle -> DomainDeviceType.AudioDongle
        NetworkDeviceType.GameConsole -> DomainDeviceType.GameConsole
        NetworkDeviceType.CastVideo -> DomainDeviceType.CastVideo
        NetworkDeviceType.CastAudio -> DomainDeviceType.CastAudio
        NetworkDeviceType.Automobile -> DomainDeviceType.Automobile
        NetworkDeviceType.Unknown -> DomainDeviceType.Unknown
    }
}

internal fun NetworkTargetDevice.toNetwork(): String? {
    return when (this) {
        NetworkTargetDevice.Current -> null
        is NetworkTargetDevice.Specific -> id
    }
}