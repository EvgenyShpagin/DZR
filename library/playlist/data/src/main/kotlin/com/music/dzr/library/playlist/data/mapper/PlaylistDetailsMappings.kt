package com.music.dzr.library.playlist.data.mapper

import com.music.dzr.library.playlist.data.remote.dto.NewPlaylistDetails as NetworkCreatedDetails
import com.music.dzr.library.playlist.data.remote.dto.PlaylistDetailsUpdate as NetworkUpdatedDetails
import com.music.dzr.library.playlist.domain.model.PlaylistDetails as DomainPlaylistDetails

internal fun DomainPlaylistDetails.toNetworkUpdate(): NetworkUpdatedDetails {
    return NetworkUpdatedDetails(
        name = name,
        public = isPublic,
        collaborative = isCollaborative,
        description = description
    )
}

internal fun DomainPlaylistDetails.toNetworkCreate(): NetworkCreatedDetails {
    return NetworkCreatedDetails(
        name = name,
        public = isPublic,
        collaborative = isCollaborative,
        description = description
    )
}
