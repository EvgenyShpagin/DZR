package com.music.dzr.player.domain.repository

import com.music.dzr.core.error.AppError
import com.music.dzr.core.pagination.CursorPage
import com.music.dzr.core.model.Market
import com.music.dzr.core.result.Result
import com.music.dzr.player.domain.model.Device
import com.music.dzr.player.domain.model.PlayHistoryEntry
import com.music.dzr.player.domain.model.PlaybackQueue
import com.music.dzr.player.domain.model.PlaybackState
import com.music.dzr.player.domain.model.RepeatMode
import kotlin.time.Instant

/**
 * Repository for player operations in the domain layer.
 *
 * All methods in this repository return a [com.music.dzr.core.result.Result] and can fail with:
 * - [com.music.dzr.core.error.ConnectivityError] for network connection issues.
 * - [com.music.dzr.core.error.NetworkError] for API-related problems (e.g., authorization, server errors).
 * Specific domain errors are documented on a per-method basis.
 */
interface PlayerRepository {

    /**
     * Get information about the user's current playback state.
     */
    suspend fun getPlaybackState(
        market: Market? = null
    ): Result<PlaybackState, AppError>

    /**
     * Transfer playback to a device.
     */
    suspend fun transferPlayback(
        deviceId: String,
        play: Boolean = false
    ): Result<Unit, AppError>

    /**
     * Get information about user's available devices.
     */
    suspend fun getAvailableDevices(): Result<List<Device>, AppError>

    /**
     * Start a new context or resume current playback.
     *
     * Domain keeps a simplified signature; specific context/options are handled by use-cases.
     */
    suspend fun startOrResumePlayback(
        deviceId: String? = null
    ): Result<Unit, AppError>

    /**
     * Pause playback.
     */
    suspend fun pausePlayback(
        deviceId: String? = null
    ): Result<Unit, AppError>

    /**
     * Seek to the given position in the currently playing track.
     */
    suspend fun seekToPosition(
        positionMs: Int,
        deviceId: String? = null
    ): Result<Unit, AppError>

    /**
     * Set the repeat mode for playback.
     */
    suspend fun setRepeatMode(
        mode: RepeatMode,
        deviceId: String? = null
    ): Result<Unit, AppError>

    /**
     * Set the volume for playback.
     */
    suspend fun setPlaybackVolume(
        volumePercent: Int,
        deviceId: String? = null
    ): Result<Unit, AppError>

    /**
     * Toggle shuffle on or off.
     */
    suspend fun setShuffle(
        enabled: Boolean,
        deviceId: String? = null
    ): Result<Unit, AppError>

    /**
     * Get the current user's recently played tracks.
     */
    suspend fun getRecentlyPlayed(
        limit: Int? = null,
        after: Instant? = null,
        before: Instant? = null
    ): Result<CursorPage<PlayHistoryEntry>, AppError>

    /**
     * Get the user's queue.
     */
    suspend fun getQueue(): Result<PlaybackQueue, AppError>

    /**
     * Add an item to the end of the user's current playback queue.
     */
    suspend fun addToQueue(
        trackId: String,
        deviceId: String? = null
    ): Result<Unit, AppError>
}
