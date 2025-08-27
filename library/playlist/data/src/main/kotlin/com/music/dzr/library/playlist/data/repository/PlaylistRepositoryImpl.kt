package com.music.dzr.library.playlist.data.repository

import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.coroutine.DispatcherProvider
import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.data.mapper.toResult
import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.Image
import com.music.dzr.core.pagination.Page
import com.music.dzr.core.result.Result
import com.music.dzr.library.playlist.data.mapper.fromDomain
import com.music.dzr.library.playlist.data.mapper.toDomain
import com.music.dzr.library.playlist.data.mapper.toNetworkCreate
import com.music.dzr.library.playlist.data.mapper.toNetworkUpdate
import com.music.dzr.library.playlist.data.remote.dto.PlaylistItemsUpdate
import com.music.dzr.library.playlist.data.remote.dto.TrackAdditions
import com.music.dzr.library.playlist.data.remote.dto.TrackRemovals
import com.music.dzr.library.playlist.data.remote.source.PlaylistRemoteDataSource
import com.music.dzr.library.playlist.domain.model.PagedPlaylist
import com.music.dzr.library.playlist.domain.model.Playlist
import com.music.dzr.library.playlist.domain.model.PlaylistDetails
import com.music.dzr.library.playlist.domain.model.PlaylistEntry
import com.music.dzr.library.playlist.domain.model.PlaylistVersion
import com.music.dzr.library.playlist.domain.model.SimplifiedPlaylist
import com.music.dzr.library.playlist.domain.repository.PlaylistRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

internal class PlaylistRepositoryImpl(
    private val remoteDataSource: PlaylistRemoteDataSource,
    private val dispatchers: DispatcherProvider,
    private val externalScope: ApplicationScope
) : PlaylistRepository {

    override suspend fun getPlaylist(
        playlistId: String,
        market: String?
    ): Result<PagedPlaylist, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getPlaylist(
                playlistId = playlistId,
                market = market
            ).toResult { it.toDomain() }
        }
    }

    override suspend fun changePlaylistDetails(
        playlistId: String,
        details: PlaylistDetails
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.changePlaylistDetails(
                    playlistId = playlistId,
                    update = details.toNetworkUpdate()
                ).toResult()
            }.await()
        }
    }

    override suspend fun getPlaylistTracks(
        playlistId: String,
        market: String?,
        limit: Int?,
        offset: Int?
    ): Result<Page<PlaylistEntry>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getPlaylistTracks(
                playlistId = playlistId,
                market = market,
                limit = limit,
                offset = offset
            ).toResult { page -> page.toDomain { it.toDomain() } }
        }
    }

    override suspend fun replaceAll(
        playlistId: String,
        newItemIds: List<String>,
        playlistVersion: PlaylistVersion?
    ): Result<PlaylistVersion, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.updatePlaylistTracks(
                    playlistId = playlistId,
                    update = PlaylistItemsUpdate.fromDomain(
                        newItemIds = newItemIds,
                        playlistVersion = playlistVersion
                    )
                ).toResult { it.toDomain() }
            }.await()
        }
    }

    override suspend fun moveRange(
        playlistId: String,
        fromIndex: Int,
        length: Int,
        toIndex: Int,
        playlistVersion: PlaylistVersion?
    ): Result<PlaylistVersion, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.updatePlaylistTracks(
                    playlistId = playlistId,
                    update = PlaylistItemsUpdate.fromDomain(
                        fromIndex = fromIndex,
                        length = length,
                        toIndex = toIndex,
                        playlistVersion = playlistVersion
                    )
                ).toResult { it.toDomain() }
            }.await()
        }
    }

    override suspend fun addTracksToPlaylist(
        playlistId: String,
        trackIds: List<String>,
        position: Int?
    ): Result<PlaylistVersion, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.addTracksToPlaylist(
                    playlistId = playlistId,
                    additions = TrackAdditions.fromDomain(
                        ids = trackIds,
                        position = position
                    )
                ).toResult { it.toDomain() }
            }.await()
        }
    }

    override suspend fun removePlaylistTracks(
        playlistId: String,
        trackIds: List<String>,
        playlistVersion: PlaylistVersion?
    ): Result<PlaylistVersion, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.removePlaylistTracks(
                    playlistId = playlistId,
                    removals = TrackRemovals.fromDomain(trackIds)
                ).toResult { it.toDomain() }
            }.await()
        }
    }

    override suspend fun getCurrentUserPlaylists(
        limit: Int?,
        offset: Int?
    ): Result<Page<SimplifiedPlaylist>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getCurrentUserPlaylists(
                limit = limit,
                offset = offset
            ).toResult { page -> page.toDomain { it.toDomain() } }
        }
    }

    override suspend fun getUserPlaylists(
        userId: String,
        limit: Int?,
        offset: Int?
    ): Result<Page<Playlist>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getUserPlaylists(
                userId = userId,
                limit = limit,
                offset = offset
            ).toResult { page -> page.toDomain { it.toDomain() } }
        }
    }

    override suspend fun createPlaylist(
        userId: String,
        details: PlaylistDetails
    ): Result<Playlist, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.createPlaylist(
                    userId = userId,
                    details = details.toNetworkCreate()
                ).toResult { it.toDomain() }
            }.await()
        }
    }

    override suspend fun getPlaylistCoverImage(
        playlistId: String
    ): Result<List<Image>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getPlaylistCoverImage(playlistId)
                .toResult { images -> images.map { it.toDomain() } }
        }
    }

    override suspend fun uploadCustomPlaylistCover(
        playlistId: String,
        jpegImage: ByteArray
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.uploadCustomPlaylistCover(
                    playlistId = playlistId,
                    jpegImageData = jpegImage
                ).toResult()
            }.await()
        }
    }
}