package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.Track
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * A service for interacting with the Spotify Track API.
 * @see <a href="https://developer.spotify.com/documentation/web-api/reference/tracks">Spotify Track API</a>
 */
interface TrackApi {
    /**
     * Get Spotify catalog information for a single track identified by its unique Spotify ID.
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/get-track">Get Track</a>
     *
     * @param id The Spotify ID of the track.
     * @param market An ISO 3166-1 alpha-2 country code.
     */
    @GET("tracks/{id}")
    suspend fun getTrack(
        @Path("id") id: String,
        @Query("market") market: String? = null
    ): NetworkResponse<Track>

} 