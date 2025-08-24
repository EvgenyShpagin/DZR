package com.music.dzr.library.playlist.data.mapper

import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.network.dto.PlaylistWithPaginatedTracks as NetworkPlaylist
import com.music.dzr.core.network.dto.PlaylistWithTracks as NetworkFullPlaylist
import com.music.dzr.core.network.dto.PlaylistWithTracksInfo as NetworkSimplifiedPlaylist
import com.music.dzr.library.playlist.domain.model.FullPlaylist as DomainFullPlaylist
import com.music.dzr.library.playlist.domain.model.PagedPlaylist as DomainPlaylist
import com.music.dzr.library.playlist.domain.model.PlaylistVersion as DomainPlaylistVersion
import com.music.dzr.library.playlist.domain.model.SimplifiedPlaylist as DomainSimplifiedPlaylist

internal fun NetworkPlaylist.toDomain(): DomainPlaylist {
    return DomainPlaylist(
        isCollaborative = collaborative,
        description = description,
        externalUrl = externalUrls.spotify,
        followersCount = followers?.total,
        id = id,
        images = images.map { it.toDomain() },
        name = name,
        owner = owner.toDomain(),
        isPublic = public,
        version = DomainPlaylistVersion.fromNetwork(snapshotId),
        entries = tracks.toDomain { it.toDomain() },
        tracksCount = tracks.total
    )
}

internal fun NetworkSimplifiedPlaylist.toDomain(): DomainSimplifiedPlaylist {
    return DomainSimplifiedPlaylist(
        isCollaborative = collaborative,
        description = description,
        externalUrl = externalUrls.spotify,
        id = id,
        images = images.map { it.toDomain() },
        name = name,
        owner = owner.toDomain(),
        isPublic = public,
        version = DomainPlaylistVersion(snapshotId),
        tracksCount = tracks.total,
        followersCount = followers?.total
    )
}

internal fun NetworkFullPlaylist.toDomain(): DomainFullPlaylist {
    return DomainFullPlaylist(
        isCollaborative = collaborative,
        description = description,
        externalUrl = externalUrls.spotify,
        followersCount = null,
        id = id,
        images = images.map { it.toDomain() },
        name = name,
        owner = owner.toDomain(),
        isPublic = public,
        version = DomainPlaylistVersion.fromNetwork(snapshotId),
        tracksCount = tracks.count(),
        entries = tracks.map { it.toDomain() }
    )
}
