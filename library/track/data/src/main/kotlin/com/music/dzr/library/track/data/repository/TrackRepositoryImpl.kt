package com.music.dzr.library.track.data.repository

import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.coroutine.DispatcherProvider
import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.data.mapper.toNetwork
import com.music.dzr.core.data.mapper.toResult
import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.Market
import com.music.dzr.core.model.Page
import com.music.dzr.core.model.Track
import com.music.dzr.core.result.Result
import com.music.dzr.library.track.data.mapper.toDomain
import com.music.dzr.library.track.data.mapper.toNetwork
import com.music.dzr.library.track.data.remote.source.TrackRemoteDataSource
import com.music.dzr.library.track.domain.model.SavedTrack
import com.music.dzr.library.track.domain.model.TimestampedId
import com.music.dzr.library.track.domain.repository.TrackRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

internal class TrackRepositoryImpl(
    private val remoteDataSource: TrackRemoteDataSource,
    private val dispatchers: DispatcherProvider,
    private val externalScope: ApplicationScope
) : TrackRepository {

    override suspend fun getTrack(
        id: String,
        market: Market?
    ): Result<Track, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getTrack(id, market?.toNetwork())
                .toResult { it.toDomain() }
        }
    }

    override suspend fun getMultipleTracks(
        ids: List<String>,
        market: Market?
    ): Result<List<Track>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getMultipleTracks(ids, market?.toNetwork())
                .toResult { tracks -> tracks.map { it.toDomain() } }
        }
    }

    override suspend fun getUserSavedTracks(
        limit: Int?,
        offset: Int?,
        market: Market?
    ): Result<Page<SavedTrack>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getUserSavedTracks(limit, offset, market?.toNetwork())
                .toResult { networkPage -> networkPage.toDomain { it.toDomain() } }
        }
    }

    override suspend fun saveTracksForUser(ids: List<String>): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.saveTracksForUser(ids).toResult()
            }.await()
        }
    }

    override suspend fun saveTracksForUserWithTimestamps(
        timestampedIds: List<TimestampedId>
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.saveTracksForUserWithTimestamps(
                    timestampedIds = timestampedIds.map { it.toNetwork() }
                ).toResult()
            }.await()
        }
    }

    override suspend fun removeTracksForUser(ids: List<String>): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.removeTracksForUser(ids).toResult()
            }.await()
        }
    }

    override suspend fun checkUserSavedTracks(ids: List<String>): Result<List<Boolean>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.checkUserSavedTracks(ids).toResult()
        }
    }
}