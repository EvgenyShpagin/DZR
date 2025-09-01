package com.music.dzr.library.player.data.repository

import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.coroutine.DispatcherProvider
import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.data.mapper.toNetwork
import com.music.dzr.core.data.mapper.toResult
import com.music.dzr.core.data.mapper.trackIdToUri
import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.Market
import com.music.dzr.core.pagination.CursorPage
import com.music.dzr.core.result.Result
import com.music.dzr.library.player.data.mapper.toDomain
import com.music.dzr.library.player.data.mapper.toNetwork
import com.music.dzr.library.player.data.remote.source.PlayerRemoteDataSource
import com.music.dzr.player.domain.model.Device
import com.music.dzr.player.domain.model.PlayHistoryEntry
import com.music.dzr.player.domain.model.PlaybackQueue
import com.music.dzr.player.domain.model.PlaybackState
import com.music.dzr.player.domain.model.RecentlyPlayedFilter
import com.music.dzr.player.domain.model.RepeatMode
import com.music.dzr.player.domain.model.TargetDevice
import com.music.dzr.player.domain.repository.PlayerRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

internal class PlayerRepositoryImpl(
    private val remoteDataSource: PlayerRemoteDataSource,
    private val dispatchers: DispatcherProvider,
    private val externalScope: ApplicationScope
) : PlayerRepository {

    override suspend fun getPlaybackState(
        market: Market
    ): Result<PlaybackState, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getPlaybackState(
                market = market.toNetwork()
            ).toResult { it.toDomain() }
        }
    }

    override suspend fun transferPlayback(
        device: TargetDevice.Specific,
        play: Boolean
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.transferPlayback(
                    deviceId = device.id,
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
        device: TargetDevice
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.startOrResumePlayback(
                    deviceId = (device as? TargetDevice.Specific)?.id,
                    options = null
                ).toResult()
            }.await()
        }
    }

    override suspend fun pausePlayback(
        device: TargetDevice
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.pausePlayback(
                    deviceId = device.toNetwork()
                ).toResult()
            }.await()
        }
    }

    override suspend fun seekToPosition(
        positionMs: Int,
        device: TargetDevice
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.seekToPosition(
                    positionMs = positionMs,
                    deviceId = device.toNetwork()
                ).toResult()
            }.await()
        }
    }

    override suspend fun setRepeatMode(
        mode: RepeatMode,
        device: TargetDevice
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.setRepeatMode(
                    state = mode.toNetwork(),
                    deviceId = device.toNetwork()
                ).toResult()
            }.await()
        }
    }

    override suspend fun setPlaybackVolume(
        volumePercent: Int,
        device: TargetDevice
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.setPlaybackVolume(
                    volumePercent = volumePercent,
                    deviceId = device.toNetwork()
                ).toResult()
            }.await()
        }
    }

    override suspend fun setShuffle(
        enabled: Boolean,
        device: TargetDevice
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.toggleShuffle(
                    state = enabled,
                    deviceId = device.toNetwork()
                ).toResult()
            }.await()
        }
    }

    override suspend fun getRecentlyPlayed(
        filter: RecentlyPlayedFilter
    ): Result<CursorPage<PlayHistoryEntry>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getRecentlyPlayedTracks(
                limit = filter.limit,
                after = (filter as? RecentlyPlayedFilter.Since)?.time,
                before = (filter as? RecentlyPlayedFilter.Until)?.time
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
        device: TargetDevice
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.addToQueue(
                    uri = trackIdToUri(trackId),
                    deviceId = device.toNetwork()
                ).toResult()
            }.await()
        }
    }
}
