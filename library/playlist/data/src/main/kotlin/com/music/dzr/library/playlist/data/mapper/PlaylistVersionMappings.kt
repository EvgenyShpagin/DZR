package com.music.dzr.library.playlist.data.mapper

import com.music.dzr.core.network.dto.SnapshotId as NetworkPlaylistVersion
import com.music.dzr.library.playlist.domain.model.PlaylistVersion as DomainPlaylistVersion

internal fun NetworkPlaylistVersion.toDomain(): DomainPlaylistVersion {
    return DomainPlaylistVersion(snapshotId)
}

internal fun DomainPlaylistVersion.Companion.fromNetwork(
    snapshotId: String
): DomainPlaylistVersion {
    return DomainPlaylistVersion(snapshotId)
}

internal fun DomainPlaylistVersion.toNetwork(): String {
    return toString()
}