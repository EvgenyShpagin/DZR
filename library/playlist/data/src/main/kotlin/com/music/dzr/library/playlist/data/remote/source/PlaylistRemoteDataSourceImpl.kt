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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.io.encoding.Base64

internal class PlaylistRemoteDataSourceImpl(
    private val playlistApi: PlaylistApi
) : PlaylistRemoteDataSource {

    override suspend fun getPlaylist(
        playlistId: String,
        market: String?,
        fields: PlaylistFields?
    ): NetworkResponse<PlaylistWithPaginatedTracks> {
        return playlistApi.getPlaylist(
            playlistId = playlistId,
            market = market,
            fields = fields
        )
    }

    override suspend fun changePlaylistDetails(
        playlistId: String,
        update: PlaylistDetailsUpdate
    ): NetworkResponse<Unit> {
        return playlistApi.changePlaylistDetails(playlistId = playlistId, update = update)
    }

    override suspend fun getPlaylistTracks(
        playlistId: String,
        market: String?,
        fields: PlaylistFields?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<PlaylistTrack>> {
        return playlistApi.getPlaylistTracks(
            playlistId = playlistId,
            market = market,
            fields = fields,
            limit = limit,
            offset = offset
        )
    }

    override suspend fun updatePlaylistTracks(
        playlistId: String,
        update: PlaylistItemsUpdate
    ): NetworkResponse<SnapshotId> {
        return playlistApi.updatePlaylistTracks(playlistId = playlistId, update = update)
    }

    override suspend fun addTracksToPlaylist(
        playlistId: String,
        body: TrackAdditions
    ): NetworkResponse<SnapshotId> {
        return playlistApi.addTracksToPlaylist(playlistId = playlistId, body = body)
    }

    override suspend fun removePlaylistTracks(
        playlistId: String,
        body: TrackRemovals
    ): NetworkResponse<SnapshotId> {
        return playlistApi.removePlaylistTracks(playlistId = playlistId, body = body)
    }

    override suspend fun getCurrentUserPlaylists(
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<PlaylistWithTracksInfo>> {
        return playlistApi.getCurrentUserPlaylists(limit = limit, offset = offset)
    }

    override suspend fun getUserPlaylists(
        userId: String,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<PlaylistWithTracks>> {
        return playlistApi.getUserPlaylists(userId = userId, limit = limit, offset = offset)
    }

    override suspend fun createPlaylist(
        userId: String,
        details: NewPlaylistDetails
    ): NetworkResponse<PlaylistWithPaginatedTracks> {
        return playlistApi.createPlaylist(userId = userId, details = details)
    }

    override suspend fun getPlaylistCoverImage(playlistId: String): NetworkResponse<List<Image>> {
        return playlistApi.getPlaylistCoverImage(playlistId = playlistId)
    }

    override suspend fun uploadCustomPlaylistCover(
        playlistId: String,
        jpegImageData: ByteArray
    ): NetworkResponse<Unit> {
        return playlistApi.uploadCustomPlaylistCover(
            playlistId = playlistId,
            encodedImageData = Base64.encode(jpegImageData)
                .toRequestBody("image/jpeg".toMediaType())
        )
    }
}