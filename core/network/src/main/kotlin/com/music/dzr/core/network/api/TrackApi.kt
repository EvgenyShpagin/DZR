package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.ApiResponse
import com.music.dzr.core.network.model.Track
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API interface for Deezer track endpoints
 *
 * @see <a href="https://developers.deezer.com/api/track">Deezer Track API Documentation</a>
 */
internal interface TrackApi {

    /**
     * Retrieves detailed information about a specific track.
     */
    @GET("track/{id}")
    suspend fun getTrack(@Path("id") trackId: Long): ApiResponse<Track>
}