package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.MusicGenre
import com.music.dzr.core.model.PopularityLevel
import com.music.dzr.core.model.DetailedArtist as DomainArtist
import com.music.dzr.core.model.SimplifiedArtist as DomainSimplifiedArtist
import com.music.dzr.core.network.dto.Artist as NetworkArtist
import com.music.dzr.core.network.dto.SimplifiedArtist as NetworkSimplifiedArtist


fun NetworkSimplifiedArtist.toDomain(): DomainSimplifiedArtist {
    return DomainSimplifiedArtist(
        id = id,
        name = name,
        externalUrl = externalUrls.spotify
    )
}

fun NetworkArtist.toDomain(): DomainArtist {
    return DomainArtist(
        id = id,
        name = name,
        externalUrl = externalUrls.spotify,
        genres = genres.map { MusicGenre.fromNetwork(it) },
        images = images.map { it.toDomain() },
        followersCount = followers.total,
        popularity = PopularityLevel.fromNetwork(popularity)
    )
}