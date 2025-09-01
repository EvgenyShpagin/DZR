package com.music.dzr.library.playlist.data.mapper

import com.music.dzr.core.data.mapper.trackIdToUri
import com.music.dzr.library.playlist.data.remote.dto.PlaylistItemsUpdate
import com.music.dzr.library.playlist.data.remote.dto.TrackAdditions
import com.music.dzr.library.playlist.data.remote.dto.TrackRemovals
import com.music.dzr.library.playlist.data.remote.dto.TrackToRemove
import com.music.dzr.library.playlist.domain.model.InsertPosition
import com.music.dzr.library.playlist.domain.model.PlaylistVersion

internal fun TrackAdditions.Companion.fromDomain(
    ids: List<String>,
    position: Int?
): TrackAdditions {
    return TrackAdditions(
        uris = ids.map(::trackIdToUri),
        position = position
    )
}

internal fun TrackRemovals.Companion.fromDomain(
    ids: List<String>
): TrackRemovals {
    return TrackRemovals(
        tracks = ids.map { trackId ->
            TrackToRemove(trackIdToUri(trackId))
        }
    )
}

internal fun PlaylistItemsUpdate.Companion.fromDomain(
    newItemIds: List<String>,
    playlistVersion: PlaylistVersion?
): PlaylistItemsUpdate {
    return PlaylistItemsUpdate(
        uris = newItemIds.map(::trackIdToUri),
        snapshotId = playlistVersion?.toNetwork()
    )
}

internal fun PlaylistItemsUpdate.Companion.fromDomain(
    fromIndex: Int,
    length: Int,
    toIndex: Int,
    playlistVersion: PlaylistVersion?
): PlaylistItemsUpdate {
    return PlaylistItemsUpdate(
        rangeStart = fromIndex,
        rangeLength = length,
        insertBefore = toIndex,
        snapshotId = playlistVersion?.toNetwork()
    )
}

internal fun InsertPosition.toNetwork(): Int? {
    return when (this) {
        InsertPosition.Append -> null
        is InsertPosition.At -> index
    }
}