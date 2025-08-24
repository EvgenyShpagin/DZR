package com.music.dzr.core.data.mapper

import com.music.dzr.core.data.remote.dto.Markets as NetworkMarkets
import com.music.dzr.core.model.AlbumOnTrack as DomainAlbumOnTrack
import com.music.dzr.core.network.dto.TrackAlbum as NetworkAlbumOnTrack

fun NetworkAlbumOnTrack.toDomain(): DomainAlbumOnTrack {
    return DomainAlbumOnTrack(
        id = id,
        name = name,
        releaseType = albumType.toDomain(),
        totalTracks = totalTracks,
        images = images.map { it.toDomain() },
        releaseDate = releaseDate.toDomain(),
        availableMarkets = NetworkMarkets(availableMarkets).toDomain(),
        externalUrl = externalUrls.spotify,
        artists = artists.map { it.toDomain() },
        restriction = restrictions?.toDomain()
    )
}