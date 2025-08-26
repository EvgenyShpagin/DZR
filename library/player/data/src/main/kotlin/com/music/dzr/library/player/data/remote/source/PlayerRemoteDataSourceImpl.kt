package com.music.dzr.library.player.data.remote.source

import com.music.dzr.core.network.dto.CursorPaginatedList
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.library.player.data.remote.api.PlayerApi
import com.music.dzr.library.player.data.remote.dto.CurrentlyPlayingContext
import com.music.dzr.library.player.data.remote.dto.Devices
import com.music.dzr.library.player.data.remote.dto.PlayHistory
import com.music.dzr.library.player.data.remote.dto.PlaybackOptions
import com.music.dzr.library.player.data.remote.dto.PlaybackState
import com.music.dzr.library.player.data.remote.dto.Queue
import com.music.dzr.library.player.data.remote.dto.RepeatMode

internal class PlayerRemoteDataSourceImpl(
    private val playerApi: PlayerApi
) : PlayerRemoteDataSource {

    override suspend fun getPlaybackState(market: String?): NetworkResponse<PlaybackState> {
        return playerApi.getPlaybackState(market)
    }

    override suspend fun transferPlayback(
        deviceId: String,
        play: Boolean
    ): NetworkResponse<Unit> {
        return playerApi.transferPlayback(mapOf("device_ids" to listOf(deviceId)), play)
    }

    override suspend fun getAvailableDevices(): NetworkResponse<Devices> {
        return playerApi.getAvailableDevices()
    }

    override suspend fun getCurrentlyPlayingTrack(
        market: String?
    ): NetworkResponse<CurrentlyPlayingContext> {
        return playerApi.getCurrentlyPlayingTrack(market)
    }

    override suspend fun startOrResumePlayback(
        deviceId: String?,
        options: PlaybackOptions?
    ): NetworkResponse<Unit> {
        return playerApi.startOrResumePlayback(deviceId, options)
    }

    override suspend fun pausePlayback(deviceId: String?): NetworkResponse<Unit> {
        return playerApi.pausePlayback(deviceId)
    }

    override suspend fun seekToPosition(
        positionMs: Int,
        deviceId: String?
    ): NetworkResponse<Unit> {
        return playerApi.seekToPosition(positionMs, deviceId)
    }

    override suspend fun setRepeatMode(
        state: RepeatMode,
        deviceId: String?
    ): NetworkResponse<Unit> {
        return playerApi.setRepeatMode(state, deviceId)
    }

    override suspend fun setPlaybackVolume(
        volumePercent: Int,
        deviceId: String?
    ): NetworkResponse<Unit> {
        return playerApi.setPlaybackVolume(volumePercent, deviceId)
    }

    override suspend fun toggleShuffle(
        state: Boolean,
        deviceId: String?
    ): NetworkResponse<Unit> {
        return playerApi.toggleShuffle(state, deviceId)
    }

    override suspend fun getRecentlyPlayedTracks(
        limit: Int?,
        after: Long?,
        before: Long?
    ): NetworkResponse<CursorPaginatedList<PlayHistory>> {
        return playerApi.getRecentlyPlayed(limit, after, before)
    }

    override suspend fun getUserQueue(): NetworkResponse<Queue> {
        return playerApi.getUserQueue()
    }

    override suspend fun addToQueue(
        uri: String,
        deviceId: String?
    ): NetworkResponse<Unit> {
        return playerApi.addToQueue(uri, deviceId)
    }
}
