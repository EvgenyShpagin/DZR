package com.music.dzr.library.artist.data.mapper

import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.model.AlbumAvailability
import com.music.dzr.core.model.Availability
import com.music.dzr.core.network.dto.AlbumGroup
import com.music.dzr.library.artist.data.remote.dto.ArtistAlbum as NetworkAlbumInDiscography
import com.music.dzr.library.artist.domain.model.AlbumInDiscography as DomainAlbumInDiscography

internal fun NetworkAlbumInDiscography.toDomain(): DomainAlbumInDiscography {
    return DomainAlbumInDiscography(
        id = id,
        availability = getAvailability(),
        name = name,
        releaseType = albumType.toDomain(),
        totalTracks = totalTracks,
        images = images.map { it.toDomain() },
        releaseDate = releaseDate.toDomain(),
        externalUrl = externalUrls.spotify,
        artists = artists.map { it.toDomain() },
        justAppearsOn = (albumGroup == AlbumGroup.AppearsOn)
    )
}

private fun NetworkAlbumInDiscography.getAvailability(): AlbumAvailability {
    return when {
        isPlayable == true -> Availability.Available
        restrictions != null -> Availability.Restricted(restrictions.toDomain())
        else -> Availability.Unknown
    }
}