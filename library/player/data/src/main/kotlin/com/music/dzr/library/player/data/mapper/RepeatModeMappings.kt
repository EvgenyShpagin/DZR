package com.music.dzr.library.player.data.mapper

import com.music.dzr.library.player.data.remote.dto.RepeatMode as NetworkRepeatMode
import com.music.dzr.player.domain.model.RepeatMode as DomainRepeatMode

internal fun DomainRepeatMode.toNetwork(): NetworkRepeatMode = when (this) {
    DomainRepeatMode.Track -> NetworkRepeatMode.Track
    DomainRepeatMode.Context -> NetworkRepeatMode.Context
    DomainRepeatMode.Off -> NetworkRepeatMode.Off
}

internal fun DomainRepeatMode.Companion.fromNetwork(repeatMode: String): DomainRepeatMode {
    return when (repeatMode.lowercase()) {
        "track" -> DomainRepeatMode.Track
        "context" -> DomainRepeatMode.Context
        else -> DomainRepeatMode.Off
    }
}
