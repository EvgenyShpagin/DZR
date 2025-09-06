package com.music.dzr.library.album.data.mapper

import com.music.dzr.core.data.mapper.fromNetwork
import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.model.AlbumAvailability
import com.music.dzr.core.model.Availability
import com.music.dzr.core.model.PopularityLevel
import com.music.dzr.core.model.TrackAvailability
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import com.music.dzr.library.album.data.remote.dto.Album as NetworkAlbum
import com.music.dzr.library.album.data.remote.dto.AlbumTrack as NetworkAlbumTrack
import com.music.dzr.library.album.domain.model.DetailedAlbum as DomainAlbum
import com.music.dzr.library.album.domain.model.TrackOnAlbum as DomainAlbumTrack

internal fun NetworkAlbum.toDomain(): DomainAlbum {
    return DomainAlbum(
        id = id,
        availability = availabilityFromNetwork(),
        name = name,
        releaseType = albumType.toDomain(),
        totalTracks = totalTracks,
        images = images.map { it.toDomain() },
        releaseDate = releaseDate.toDomain(),
        externalUrl = externalUrls.spotify,
        artists = artists.map { it.toDomain() },
        tracks = tracks.items.map { it.toDomain() },
        copyrights = copyrights.map { it.toDomain() },
        externalIds = externalIds.toDomain(),
        genres = emptyList(),
        label = label,
        popularity = PopularityLevel.fromNetwork(popularity)
    )
}

private fun NetworkAlbum.availabilityFromNetwork(): AlbumAvailability {
    return Availability.fromNetwork(
        isPlayable = isPlayable,
        restrictions = restrictions
    ) as AlbumAvailability
}

internal fun NetworkAlbumTrack.toDomain(): DomainAlbumTrack {
    return DomainAlbumTrack(
        id = id,
        availability = trackAvailabilityFromNetwork(),
        name = name,
        artists = artists.map { it.toDomain() },
        duration = durationMs.toDuration(DurationUnit.MILLISECONDS),
        trackNumber = trackNumber,
        discNumber = discNumber,
        isExplicit = explicit,
        externalUrl = externalUrls.spotify
    )
}

private fun NetworkAlbumTrack.trackAvailabilityFromNetwork(): TrackAvailability {
    return Availability.fromNetwork(
        isPlayable = isPlayable,
        restrictions = restrictions,
        isLocal = isLocal,
        linkedFrom = linkedFrom
    ) as TrackAvailability
}
