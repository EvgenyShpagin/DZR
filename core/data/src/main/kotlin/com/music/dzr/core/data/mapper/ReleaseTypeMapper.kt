package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.ReleaseType as DomainReleaseType
import com.music.dzr.core.network.dto.AlbumType as NetworkReleaseType

fun NetworkReleaseType.toDomain(): DomainReleaseType {
    return when (this) {
        NetworkReleaseType.Album -> DomainReleaseType.ALBUM
        NetworkReleaseType.Single -> DomainReleaseType.SINGLE
        NetworkReleaseType.Compilation -> DomainReleaseType.COMPILATION
    }
}