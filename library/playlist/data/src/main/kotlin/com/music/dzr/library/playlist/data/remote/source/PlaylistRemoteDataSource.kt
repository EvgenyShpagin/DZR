package com.music.dzr.library.playlist.data.remote.source

import com.music.dzr.core.network.dto.Image
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.PlaylistTrack
import com.music.dzr.core.network.dto.PlaylistWithPaginatedTracks
import com.music.dzr.core.network.dto.PlaylistWithTracks
import com.music.dzr.core.network.dto.PlaylistWithTracksInfo
import com.music.dzr.core.network.dto.SnapshotId
import com.music.dzr.library.playlist.data.remote.api.PlaylistApi
import com.music.dzr.library.playlist.data.remote.dto.NewPlaylistDetails
import com.music.dzr.library.playlist.data.remote.dto.PlaylistDetailsUpdate
import com.music.dzr.library.playlist.data.remote.dto.PlaylistFields
import com.music.dzr.library.playlist.data.remote.dto.PlaylistItemsUpdate
import com.music.dzr.library.playlist.data.remote.dto.TrackAdditions
import com.music.dzr.library.playlist.data.remote.dto.TrackRemovals

/**
 * Remote data source for playlist-related operations.
 * Thin wrapper around [PlaylistApi] with convenient method signatures.
 */
internal interface PlaylistRemoteDataSource {

    suspend fun getPlaylist(
        playlistId: String,
        market: String? = null,
        fields: PlaylistFields? = null
    ): NetworkResponse<PlaylistWithPaginatedTracks>

    suspend fun changePlaylistDetails(
        playlistId: String,
        update: PlaylistDetailsUpdate
    ): NetworkResponse<Unit>

    suspend fun getPlaylistTracks(
        playlistId: String,
        market: String? = null,
        fields: PlaylistFields? = null,
        limit: Int? = null,
        offset: Int? = null
    ): NetworkResponse<PaginatedList<PlaylistTrack>>

    suspend fun updatePlaylistTracks(
        playlistId: String,
        update: PlaylistItemsUpdate
    ): NetworkResponse<SnapshotId>

    suspend fun addTracksToPlaylist(
        playlistId: String,
        body: TrackAdditions
    ): NetworkResponse<SnapshotId>

    suspend fun removePlaylistTracks(
        playlistId: String,
        body: TrackRemovals
    ): NetworkResponse<SnapshotId>

    suspend fun getCurrentUserPlaylists(
        limit: Int? = null,
        offset: Int? = null
    ): NetworkResponse<PaginatedList<PlaylistWithTracksInfo>>

    suspend fun getUserPlaylists(
        userId: String,
        limit: Int? = null,
        offset: Int? = null
    ): NetworkResponse<PaginatedList<PlaylistWithTracks>>

    suspend fun createPlaylist(
        userId: String,
        details: NewPlaylistDetails
    ): NetworkResponse<PlaylistWithPaginatedTracks>

    suspend fun getPlaylistCoverImage(playlistId: String): NetworkResponse<List<Image>>

    suspend fun uploadCustomPlaylistCover(
        playlistId: String,
        jpegImageData: ByteArray
    ): NetworkResponse<Unit>
}