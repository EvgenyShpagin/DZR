package com.music.dzr.library.player.data.mapper

import com.music.dzr.library.player.data.remote.dto.Device as NetworkDevice
import com.music.dzr.library.player.data.remote.dto.DeviceType as NetworkDeviceType
import com.music.dzr.player.domain.model.Device as DomainDevice
import com.music.dzr.player.domain.model.DeviceType as DomainDeviceType

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
