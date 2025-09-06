package com.music.dzr.library.album.data.repository

import com.music.dzr.core.coroutine.DispatcherProvider
import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.data.mapper.toNetwork
import com.music.dzr.core.data.mapper.toResult
import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.Market
import com.music.dzr.core.pagination.OffsetPageable
import com.music.dzr.core.pagination.Page
import com.music.dzr.core.result.Result
import com.music.dzr.library.album.data.mapper.toDomain
import com.music.dzr.library.album.data.remote.source.AlbumRemoteDataSource
import com.music.dzr.library.album.domain.model.DetailedAlbum
import com.music.dzr.library.album.domain.model.TrackOnAlbum
import com.music.dzr.library.album.domain.repository.AlbumRepository
import kotlinx.coroutines.withContext

internal class AlbumRepositoryImpl(
    private val remoteDataSource: AlbumRemoteDataSource,
    private val dispatchers: DispatcherProvider
) : AlbumRepository {

    override suspend fun getAlbum(
        id: String,
        market: Market
    ): Result<DetailedAlbum, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getAlbum(
                id = id,
                market = market.toNetwork()
            ).toResult { it.toDomain() }
        }
    }

    override suspend fun getMultipleAlbums(
        ids: List<String>,
        market: Market
    ): Result<List<DetailedAlbum>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getMultipleAlbums(
                ids = ids,
                market = market.toNetwork()
            ).toResult { wrapper -> wrapper.list.map { it.toDomain() } }
        }
    }

    override suspend fun getAlbumTracks(
        id: String,
        market: Market,
        pageable: OffsetPageable
    ): Result<Page<TrackOnAlbum>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getAlbumTracks(
                id = id,
                market = market.toNetwork(),
                limit = pageable.limit,
                offset = pageable.offset
            ).toResult { page ->
                page.toDomain { it.toDomain() }
            }
        }
    }

    override suspend fun saveAlbumsForUser(ids: List<String>): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.saveAlbumsForUser(ids).toResult()
        }
    }

    override suspend fun removeAlbumsForUser(ids: List<String>): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.removeAlbumsForUser(ids).toResult()
        }
    }

    override suspend fun getUserSavedAlbums(
        market: Market,
        pageable: OffsetPageable
    ): Result<Page<DetailedAlbum>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getUserSavedAlbums(
                limit = pageable.limit,
                offset = pageable.offset,
                market = market.toNetwork()
            ).toResult { page ->
                page.toDomain { saved -> saved.album.toDomain() }
            }
        }
    }
}
