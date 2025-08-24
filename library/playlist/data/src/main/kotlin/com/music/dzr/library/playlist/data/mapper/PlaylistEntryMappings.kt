package com.music.dzr.library.playlist.data.mapper

import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.network.dto.PlaylistTrack as NetworkPlaylistEntry
import com.music.dzr.library.playlist.domain.model.PlaylistEntry as DomainPlaylistEntry

internal fun NetworkPlaylistEntry.toDomain(): DomainPlaylistEntry {
    return DomainPlaylistEntry(
        addedAt = addedAt,
        addedBy = addedBy.toDomain(),
        isLocal = isLocal,
        track = track.toDomain(),
    )
}
