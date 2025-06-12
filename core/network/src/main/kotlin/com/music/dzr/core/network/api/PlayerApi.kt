package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.PermissionScope
import com.music.dzr.core.network.model.PlaybackState
import retrofit2.http.GET
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

} 