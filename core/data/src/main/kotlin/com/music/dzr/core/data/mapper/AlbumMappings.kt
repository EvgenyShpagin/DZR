package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.AlbumAvailability
import com.music.dzr.core.model.Availability
import com.music.dzr.core.model.AlbumOnTrack as DomainAlbumOnTrack
import com.music.dzr.core.network.dto.TrackAlbum as NetworkAlbumOnTrack

fun NetworkAlbumOnTrack.toDomain(): DomainAlbumOnTrack {
    return DomainAlbumOnTrack(
        id = id,
        availability = availabilityFromNetwork(),
        name = name,
        releaseType = albumType.toDomain(),
        totalTracks = totalTracks,
        images = images.map { it.toDomain() },
        releaseDate = releaseDate.toDomain(),
        externalUrl = externalUrls.spotify,
        artists = artists.map { it.toDomain() },
    )
}

private fun NetworkAlbumOnTrack.availabilityFromNetwork(): AlbumAvailability {
    return Availability.fromNetwork(
        isPlayable = isPlayable,
        restrictions = restrictions
    ) as AlbumAvailability
}