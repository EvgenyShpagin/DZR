package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.DevicesContainer
import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.PermissionScope
import com.music.dzr.core.network.model.PlaybackState
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

/**
 * A service for interacting with the Spotify Player API.
 */
interface PlayerApi {

    /**
     * Get information about the user's current playback state, including track or episode, progress, and active device.
     *
     * Requires [PermissionScope.UserReadPlaybackState]
     *
     * @param market An ISO 3166-1 alpha-2 country code.
     */
    @GET("me/player")
    suspend fun getPlaybackState(
        @Query("market") market: String? = null,
    ): NetworkResponse<PlaybackState>

    /**
     * Transfer playback to a new device and determine if it should start playing.
     *
     * Requires [PermissionScope.UserModifyPlaybackState]
     *
     * @param deviceIds A JSON object containing a single key "device_ids" which is an array of device IDs to transfer playback to.
     * **Only a single `device_id` is currently supported**
     * @param play `true`: ensure playback happens on new device. `false`: keep the current playback state.
     */
    @PUT("me/player")
    suspend fun transferPlayback(
        @Body deviceIds: Map<String, List<String>>,
        @Query("play") play: Boolean = false
    ): NetworkResponse<Unit>

    /**
     * Get information about a user's available devices.
     *
     * Requires [PermissionScope.UserReadPlaybackState]
     */
    @GET("me/player/devices")
    suspend fun getAvailableDevices(): NetworkResponse<DevicesContainer>

    /**
     * Get the object for the user's currently playing track.
     *
     * Requires [PermissionScope.UserReadCurrentlyPlaying]
     *
     * @param market An ISO 3166-1 alpha-2 country code.
     */
    @GET("me/player/currently-playing")
    suspend fun getCurrentlyPlayingTrack(
        @Query("market") market: String? = null,
    ): NetworkResponse<PlaybackState>

} 