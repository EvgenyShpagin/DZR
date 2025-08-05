package com.music.dzr.library.player.data.remote.api

import com.music.dzr.core.network.model.CursorPaginatedList
import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.library.player.data.remote.dto.CurrentlyPlayingContext
import com.music.dzr.library.player.data.remote.dto.Devices
import com.music.dzr.library.player.data.remote.dto.PlayHistory
import com.music.dzr.library.player.data.remote.dto.PlaybackOptions
import com.music.dzr.library.player.data.remote.dto.PlaybackState
import com.music.dzr.library.player.data.remote.dto.Queue
import com.music.dzr.library.player.data.remote.dto.RepeatMode
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

/**
 * A service for interacting with the Spotify Player API.
 * @see <a href="https://developer.spotify.com/documentation/web-api/">Spotify API</a>
 */
interface PlayerApi {

    /**
     * Get information about the user's current playback state, including track or episode, progress, and active device.
     *
     * Requires permission `UserReadPlaybackState`
     *
     * @param market An ISO 3166-1 alpha-2 country code.
     */
    @GET("me/player")
    suspend fun getPlaybackState(
        @Query("market") market: String? = null
    ): NetworkResponse<PlaybackState>

    /**
     * Transfer playback to a new device and determine if it should start playing.
     *
     * Requires permission `UserModifyPlaybackState`
     *
     * @param deviceIds A JSON object containing a single key "device_ids" which is an array of device IDs to transfer playback to.
     * **Only a single `device_id` is currently supported**
     * @param play `true`: ensure playback happens on new device. `false`: keep the current playback state.
     */
    @PUT("me/player")
    suspend fun transferPlayback(
        @Body deviceIds: Map<String, @JvmSuppressWildcards List<String>>,
        @Query("play") play: Boolean = false
    ): NetworkResponse<Unit>

    /**
     * Get information about a user's available devices.
     *
     * Requires permission `UserReadPlaybackState`
     */
    @GET("me/player/devices")
    suspend fun getAvailableDevices(): NetworkResponse<Devices>

    /**
     * Get the object for the user's currently playing track.
     *
     * Requires permission `UserReadCurrentlyPlaying`
     *
     * @param market An ISO 3166-1 alpha-2 country code.
     */
    @GET("me/player/currently-playing")
    suspend fun getCurrentlyPlayingTrack(
        @Query("market") market: String? = null,
    ): NetworkResponse<CurrentlyPlayingContext>

    /**
     * Start a new context or resume current playback on the user's active device.
     *
     * Requires permission `UserModifyPlaybackState`
     *
     * @param deviceId The id of the device this command is targeting.
     * @param body The playback options.
     */
    @PUT("me/player/play")
    suspend fun startOrResumePlayback(
        @Query("device_id") deviceId: String? = null,
        @Body body: PlaybackOptions? = null
    ): NetworkResponse<Unit>

    /**
     * Pause playback on the user's account.
     *
     * Requires permission `UserModifyPlaybackState`
     *
     * @param deviceId The id of the device this command is targeting.
     */
    @PUT("me/player/pause")
    suspend fun pausePlayback(
        @Query("device_id") deviceId: String? = null
    ): NetworkResponse<Unit>

    /**
     * Skips to next track in the user's queue.
     *
     * Requires permission `UserModifyPlaybackState`
     *
     * @param deviceId The id of the device this command is targeting.
     */
    @POST("me/player/next")
    suspend fun skipToNext(
        @Query("device_id") deviceId: String? = null
    ): NetworkResponse<Unit>

    /**
     * Skips to previous track in the user's queue.
     *
     * Requires permission `UserModifyPlaybackState`
     *
     * @param deviceId The id of the device this command is targeting.
     */
    @POST("me/player/previous")
    suspend fun skipToPrevious(
        @Query("device_id") deviceId: String? = null
    ): NetworkResponse<Unit>

    /**
     * Seeks to the given position in the user's currently playing track.
     *
     * Requires permission `UserModifyPlaybackState`
     *
     * @param positionMs The position in milliseconds to seek to.
     * @param deviceId The id of the device this command is targeting.
     */
    @PUT("me/player/seek")
    suspend fun seekToPosition(
        @Query("position_ms") positionMs: Int,
        @Query("device_id") deviceId: String? = null
    ): NetworkResponse<Unit>

    /**
     * Set the repeat mode for the user's playback.
     *
     * Requires permission `UserModifyPlaybackState`
     *
     * @param state The repeat mode.
     * @param deviceId The id of the device this command is targeting.
     */
    @PUT("me/player/repeat")
    suspend fun setRepeatMode(
        @Query("state") state: RepeatMode,
        @Query("device_id") deviceId: String? = null
    ): NetworkResponse<Unit>

    /**
     * Set the volume for the user's playback.
     *
     * Requires permission `UserModifyPlaybackState`
     *
     * @param volumePercent The volume to set. Must be a value from 0 to 100.
     * @param deviceId The id of the device this command is targeting.
     */
    @PUT("me/player/volume")
    suspend fun setPlaybackVolume(
        @Query("volume_percent") volumePercent: Int,
        @Query("device_id") deviceId: String? = null
    ): NetworkResponse<Unit>

    /**
     * Toggle shuffle on or off for user's playback.
     *
     * Requires permission `UserModifyPlaybackState`
     *
     * @param state `true` : Turn shuffle on. `false` : Turn shuffle off.
     * @param deviceId The id of the device this command is targeting.
     */
    @PUT("me/player/shuffle")
    suspend fun toggleShuffle(
        @Query("state") state: Boolean,
        @Query("device_id") deviceId: String? = null
    ): NetworkResponse<Unit>

    /**
     * Get tracks from the current user's recently played tracks.
     *
     * Requires permission `UserReadRecentlyPlayed`
     *
     * @param limit The maximum number of items to return.
     * @param after A Unix timestamp in milliseconds. Returns items after this time.
     * @param before A Unix timestamp in milliseconds. Returns items before this time.
     */
    @GET("me/player/recently-played")
    suspend fun getRecentlyPlayed(
        @Query("limit") limit: Int? = null,
        @Query("after") after: Long? = null,
        @Query("before") before: Long? = null
    ): NetworkResponse<CursorPaginatedList<PlayHistory>>

    /**
     * Get the list of objects that make up the user's queue.
     *
     * Requires permissions:
     * - `UserReadCurrentlyPlaying`
     * - `UserReadPlaybackState`
     */
    @GET("me/player/queue")
    suspend fun getUserQueue(): NetworkResponse<Queue>

    /**
     * Add an item to the end of the user's current playback queue.
     *
     * Requires permission `UserModifyPlaybackState`
     *
     * @param uri The uri of the item to add to the queue.
     * @param deviceId The id of the device this command is targeting.
     */
    @POST("me/player/queue")
    suspend fun addToQueue(
        @Query("uri") uri: String,
        @Query("device_id") deviceId: String? = null
    ): NetworkResponse<Unit>
} 