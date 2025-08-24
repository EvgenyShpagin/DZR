package com.music.dzr.library.playlist.data.mapper

import com.music.dzr.library.playlist.data.remote.dto.PlaylistItemsUpdate
import com.music.dzr.library.playlist.data.remote.dto.TrackAdditions
import com.music.dzr.library.playlist.data.remote.dto.TrackRemovals
import com.music.dzr.library.playlist.data.remote.dto.TrackToRemove
import com.music.dzr.library.playlist.domain.model.PlaylistVersion

internal fun TrackAdditions.Companion.fromDomain(
    ids: List<String>,
    position: Int?
): TrackAdditions {
    return TrackAdditions(
        uris = ids.map(::playlistIdToUri),
        position = position
    )
}

internal fun TrackRemovals.Companion.fromDomain(
    ids: List<String>
): TrackRemovals {
    return TrackRemovals(
        tracks = ids.map { trackId ->
            TrackToRemove(playlistIdToUri(trackId))
        }
    )
}

internal fun PlaylistItemsUpdate.Companion.fromDomain(
    newItemIds: List<String>,
    playlistVersion: PlaylistVersion?
): PlaylistItemsUpdate {
    return PlaylistItemsUpdate(
        uris = newItemIds.map(::playlistIdToUri),
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

private fun playlistIdToUri(id: String): String {
    return "spotify:playlist:$id"
}