package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.Availability
import com.music.dzr.core.model.PopularityLevel
import com.music.dzr.core.model.TrackAvailability
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import com.music.dzr.core.model.DetailedTrack as DomainTrack
import com.music.dzr.core.network.dto.Track as NetworkTrack

fun NetworkTrack.toDomain(): DomainTrack {
    return DomainTrack(
        id = id,
        availability = availabilityFromNetwork(),
        name = name,
        artists = artists.map { it.toDomain() },
        duration = durationMs.toDuration(DurationUnit.MILLISECONDS),
        trackNumber = trackNumber,
        discNumber = discNumber,
        isExplicit = explicit,
        externalUrl = externalUrls.spotify,
        album = album.toDomain(),
        popularity = PopularityLevel.fromNetwork(popularity),
        externalIds = externalIds.toDomain()
    )
}

private fun NetworkTrack.availabilityFromNetwork(): TrackAvailability {
    return Availability.fromNetwork(
        isPlayable = isPlayable,
        restrictions = restrictions,
        isLocal = isLocal,
        linkedFrom = linkedFrom
    ) as TrackAvailability
}