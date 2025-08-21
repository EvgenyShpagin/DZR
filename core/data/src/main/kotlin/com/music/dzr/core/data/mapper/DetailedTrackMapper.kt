package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.PopularityLevel
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import com.music.dzr.core.model.DetailedTrack as DomainTrack
import com.music.dzr.core.network.dto.Track as NetworkTrack

fun NetworkTrack.toDomain(): DomainTrack {
    return DomainTrack(
        id = id,
        name = name,
        artists = artists.map { it.toDomain() },
        duration = durationMs.toDuration(DurationUnit.MILLISECONDS),
        trackNumber = trackNumber,
        discNumber = discNumber,
        isExplicit = explicit,
        isPlayable = isPlayable == true,
        isLocal = isLocal,
        externalUrl = externalUrls.spotify,
        restriction = restrictions?.toDomain(),
        album = album.toDomain(),
        popularity = PopularityLevel.parse(popularity),
        externalIds = externalIds.toDomain()
    )
}