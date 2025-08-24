package com.music.dzr.library.track.data.mapper

import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.library.track.data.remote.dto.SavedTrack as NetworkSavedTrack
import com.music.dzr.library.track.domain.model.SavedTrack as DomainSavedTrack

internal fun NetworkSavedTrack.toDomain(): DomainSavedTrack {
    return DomainSavedTrack(
        addedAt = addedAt,
        music = track.toDomain()
    )
}