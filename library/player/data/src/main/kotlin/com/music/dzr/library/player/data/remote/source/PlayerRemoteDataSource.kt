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

/**
 * Remote data source for player-related operations.
 * Thin wrapper around [PlayerApi] with convenient method signatures and parameter preparation.
 */
internal class PlayerRemoteDataSource(private val playerApi: PlayerApi) {

    /**
     * Get information about the user's current playback state.
     */
    suspend fun getPlaybackState(market: String? = null): NetworkResponse<PlaybackState> {
        return playerApi.getPlaybackState(market)
    }

    /**
     * Transfer playback to a new device.
     */
    suspend fun transferPlayback(deviceId: String, play: Boolean = false): NetworkResponse<Unit> {
        return playerApi.transferPlayback(mapOf("device_ids" to listOf(deviceId)), play)
    }

    /**
     * Get information about a user's available devices.
     */
    suspend fun getAvailableDevices(): NetworkResponse<Devices> {
        return playerApi.getAvailableDevices()
    }

    /**
     * Get the user's currently playing track.
     */
    suspend fun getCurrentlyPlayingTrack(
        market: String? = null
    ): NetworkResponse<CurrentlyPlayingContext> {
        return playerApi.getCurrentlyPlayingTrack(market)
    }

    /**
     * Start a new context or resume current playback.
     */
    suspend fun startOrResumePlayback(
        deviceId: String? = null,
        options: PlaybackOptions? = null
    ): NetworkResponse<Unit> {
        return playerApi.startOrResumePlayback(deviceId, options)
    }

    /**
     * Pause playback.
     */
    suspend fun pausePlayback(deviceId: String? = null): NetworkResponse<Unit> {
        return playerApi.pausePlayback(deviceId)
    }

    /**
     * Seek to the given position in the currently playing track.
     */
    suspend fun seekToPosition(positionMs: Int, deviceId: String? = null): NetworkResponse<Unit> {
        return playerApi.seekToPosition(positionMs, deviceId)
    }

    /**
     * Set the repeat mode for the user's playback.
     */
    suspend fun setRepeatMode(state: RepeatMode, deviceId: String? = null): NetworkResponse<Unit> {
        return playerApi.setRepeatMode(state, deviceId)
    }

    /**
     * Set the volume for the user's playback.
     */
    suspend fun setPlaybackVolume(
        volumePercent: Int,
        deviceId: String? = null
    ): NetworkResponse<Unit> {
        return playerApi.setPlaybackVolume(volumePercent, deviceId)
    }

    /**
     * Toggle shuffle on or off for user's playback.
     */
    suspend fun toggleShuffle(state: Boolean, deviceId: String? = null): NetworkResponse<Unit> {
        return playerApi.toggleShuffle(state, deviceId)
    }

    /**
     * Get the current user's recently played tracks.
     */
    suspend fun getRecentlyPlayedTracks(
        limit: Int? = null,
        after: Long? = null,
        before: Long? = null
    ): NetworkResponse<CursorPaginatedList<PlayHistory>> {
        return playerApi.getRecentlyPlayed(limit, after, before)
    }

    /**
     * Get the user's queue.
     */
    suspend fun getUserQueue(): NetworkResponse<Queue> {
        return playerApi.getUserQueue()
    }

    /**
     * Add an item to the end of the user's current playback queue.
     */
    suspend fun addToQueue(uri: String, deviceId: String? = null): NetworkResponse<Unit> {
        return playerApi.addToQueue(uri, deviceId)
    }
}