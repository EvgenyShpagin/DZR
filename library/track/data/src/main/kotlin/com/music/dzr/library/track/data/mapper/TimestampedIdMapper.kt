package com.music.dzr.library.track.data.mapper

import com.music.dzr.library.track.data.remote.dto.TimestampedId as NetworkTimestampedId
import com.music.dzr.library.track.domain.model.TimestampedId as DomainTimestampedId

internal fun DomainTimestampedId.toNetwork(): NetworkTimestampedId {
    return NetworkTimestampedId(id = id, addedAt = addedAt)
}