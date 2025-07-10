package com.music.dzr.core.model.device

/**
 * Types of devices that can be used for music playback.
 *
 * Each device type has different capabilities and characteristics that affect
 * how playback controls and features are presented to the user.
 *
 * @see Device
 */

enum class DeviceType {
    Computer,
    Tablet,
    Smartphone,
    Speaker,
    Tv,
    AudioVideoReceiver,
    SetTopBox,
    AudioDongle,
    GameConsole,
    CastVideo,
    CastAudio,
    Automobile,
    Unknown
}