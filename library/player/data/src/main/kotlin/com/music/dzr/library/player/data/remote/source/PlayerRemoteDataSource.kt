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
import kotlin.time.Instant

/**
 * Remote data source for player-related operations.
 * Thin wrapper around [PlayerApi] with convenient method signatures and parameter preparation.
 */
internal interface PlayerRemoteDataSource {

    /**
     * Get information about the user's current playback state.
     */
    suspend fun getPlaybackState(market: String? = null): NetworkResponse<PlaybackState>

    /**
     * Transfer playback to a new device.
     */
    suspend fun transferPlayback(deviceId: String, play: Boolean = false): NetworkResponse<Unit>

    /**
     * Get information about a user's available devices.
     */
    suspend fun getAvailableDevices(): NetworkResponse<Devices>

    /**
     * Get the user's currently playing track.
     */
    suspend fun getCurrentlyPlayingTrack(
        market: String? = null
    ): NetworkResponse<CurrentlyPlayingContext>

    /**
     * Start a new context or resume current playback.
     */
    suspend fun startOrResumePlayback(
        deviceId: String? = null,
        options: PlaybackOptions? = null
    ): NetworkResponse<Unit>

    /**
     * Pause playback.
     */
    suspend fun pausePlayback(deviceId: String? = null): NetworkResponse<Unit>

    /**
     * Seek to the given position in the currently playing track.
     */
    suspend fun seekToPosition(positionMs: Int, deviceId: String? = null): NetworkResponse<Unit>

    /**
     * Set the repeat mode for the user's playback.
     */
    suspend fun setRepeatMode(state: RepeatMode, deviceId: String? = null): NetworkResponse<Unit>

    /**
     * Set the volume for the user's playback.
     */
    suspend fun setPlaybackVolume(
        volumePercent: Int,
        deviceId: String? = null
    ): NetworkResponse<Unit>

    /**
     * Toggle shuffle on or off for user's playback.
     */
    suspend fun toggleShuffle(state: Boolean, deviceId: String? = null): NetworkResponse<Unit>

    /**
     * Get the current user's recently played tracks.
     */
    suspend fun getRecentlyPlayedTracks(
        limit: Int? = null,
        after: Instant? = null,
        before: Instant? = null
    ): NetworkResponse<CursorPaginatedList<PlayHistory>>

    /**
     * Get the user's queue.
     */
    suspend fun getUserQueue(): NetworkResponse<Queue>

    /**
     * Add an item to the end of the user's current playback queue.
     */
    suspend fun addToQueue(uri: String, deviceId: String? = null): NetworkResponse<Unit>
}