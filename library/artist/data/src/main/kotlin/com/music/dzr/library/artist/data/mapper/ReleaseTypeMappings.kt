package com.music.dzr.library.artist.data.mapper

import com.music.dzr.core.model.ReleaseType as DomainReleaseType
import com.music.dzr.core.network.dto.AlbumGroup as NetworkAlbumGroup

internal fun List<DomainReleaseType>.toNetwork(
    includeAppearsOn: Boolean
): List<NetworkAlbumGroup>? {
    val mappedGroups = map { it.toNetworkAlbumGroup() }

    val allGroups = if (includeAppearsOn) {
        mappedGroups + NetworkAlbumGroup.AppearsOn
    } else {
        mappedGroups
    }

    // Return null if all possible groups are included (API expects null for "all")
    return allGroups.takeUnless { it.size == NetworkAlbumGroup.entries.size }
}

private fun DomainReleaseType.toNetworkAlbumGroup(): NetworkAlbumGroup {
    return when (this) {
        DomainReleaseType.ALBUM -> NetworkAlbumGroup.Album
        DomainReleaseType.SINGLE -> NetworkAlbumGroup.Single
        DomainReleaseType.EP,
        DomainReleaseType.COMPILATION -> NetworkAlbumGroup.Compilation
    }
}