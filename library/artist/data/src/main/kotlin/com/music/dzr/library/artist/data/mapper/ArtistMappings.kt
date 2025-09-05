package com.music.dzr.library.artist.data.mapper

import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.model.DetailedArtist as DomainDetailedArtist
import com.music.dzr.library.artist.data.remote.dto.Artists as NetworkDetailedArtists

internal fun NetworkDetailedArtists.toDomain(): List<DomainDetailedArtist> {
    return list.map { artist -> artist.toDomain() }
}
