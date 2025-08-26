package com.music.dzr.library.player.data.mapper

import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.library.player.data.remote.dto.PlayHistory as NetworkPlayHistoryEntry
import com.music.dzr.player.domain.model.PlayHistoryEntry as DomainPlayHistoryEntry

internal fun NetworkPlayHistoryEntry.toDomain(): DomainPlayHistoryEntry {
    return DomainPlayHistoryEntry(
        track = track.toDomain(),
        playedAt = playedAt,
        context = context?.toDomain()
    )
}
