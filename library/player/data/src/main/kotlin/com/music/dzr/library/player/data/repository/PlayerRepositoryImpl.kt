package com.music.dzr.library.player.data.repository

import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.coroutine.DispatcherProvider
import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.data.mapper.toNetwork
import com.music.dzr.core.data.mapper.toResult
import com.music.dzr.core.data.mapper.trackIdToUri
import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.CursorPage
import com.music.dzr.core.model.Market
import com.music.dzr.core.result.Result
import com.music.dzr.library.player.data.mapper.toDomain
import com.music.dzr.library.player.data.mapper.toNetwork
import com.music.dzr.library.player.data.remote.source.PlayerRemoteDataSource
import com.music.dzr.player.domain.model.Device
import com.music.dzr.player.domain.model.PlayHistoryEntry
import com.music.dzr.player.domain.model.PlaybackQueue
import com.music.dzr.player.domain.model.PlaybackState
import com.music.dzr.player.domain.model.RepeatMode
import com.music.dzr.player.domain.repository.PlayerRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Instant

internal class PlayerRepositoryImpl(
    private val remoteDataSource: PlayerRemoteDataSource,
    private val dispatchers: DispatcherProvider,
    private val externalScope: ApplicationScope
) : PlayerRepository {

    override suspend fun getPlaybackState(
        market: Market?
    ): Result<PlaybackState, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getPlaybackState(
                market = market?.toNetwork()
            ).toResult { it.toDomain() }
        }
    }

    override suspend fun transferPlayback(
        deviceId: String,
        play: Boolean
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.transferPlayback(
                    deviceId = deviceId,
                    play = play
                ).toResult()
            }.await()
        }
    }

    override suspend fun getAvailableDevices(): Result<List<Device>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getAvailableDevices()
                .toResult { devices ->
                    devices.toDomain()
                }
        }
    }

    override suspend fun startOrResumePlayback(
        deviceId: String?
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.startOrResumePlayback(
                    deviceId = deviceId,
                    options = null
                ).toResult()
            }.await()
        }
    }

    override suspend fun pausePlayback(
        deviceId: String?
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.pausePlayback(
                    deviceId = deviceId
                ).toResult()
            }.await()
        }
    }

    override suspend fun seekToPosition(
        positionMs: Int,
        deviceId: String?
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.seekToPosition(
                    positionMs = positionMs,
                    deviceId = deviceId
                ).toResult()
            }.await()
        }
    }

    override suspend fun setRepeatMode(
        mode: RepeatMode,
        deviceId: String?
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.setRepeatMode(
                    state = mode.toNetwork(),
                    deviceId = deviceId
                ).toResult()
            }.await()
        }
    }

    override suspend fun setPlaybackVolume(
        volumePercent: Int,
        deviceId: String?
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.setPlaybackVolume(
                    volumePercent = volumePercent,
                    deviceId = deviceId
                ).toResult()
            }.await()
        }
    }

    override suspend fun setShuffle(
        enabled: Boolean,
        deviceId: String?
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.toggleShuffle(
                    state = enabled,
                    deviceId = deviceId
                ).toResult()
            }.await()
        }
    }

    override suspend fun getRecentlyPlayed(
        limit: Int?,
        after: Instant?,
        before: Instant?
    ): Result<CursorPage<PlayHistoryEntry>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getRecentlyPlayedTracks(
                limit = limit,
                after = after,
                before = before
            ).toResult { page ->
                page.toDomain { it.toDomain() }
            }
        }
    }

    override suspend fun getQueue(): Result<PlaybackQueue, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getUserQueue().toResult { it.toDomain() }
        }
    }

    override suspend fun addToQueue(
        trackId: String,
        deviceId: String?
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.addToQueue(
                    uri = trackIdToUri(trackId),
                    deviceId = deviceId
                ).toResult()
            }.await()
        }
    }
}
