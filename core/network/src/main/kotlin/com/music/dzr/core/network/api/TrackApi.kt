package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.PermissionScope
import com.music.dzr.core.network.model.SavedTrack
import com.music.dzr.core.network.model.Track
import com.music.dzr.core.network.model.TracksContainer
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

    /**
     * Get Spotify catalog information for multiple tracks based on their Spotify IDs.
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/get-several-tracks">Get Several Tracks</a>
     *
     * @param ids A comma-separated list of the Spotify IDs for the tracks. Maximum: 50.
     * @param market An ISO 3166-1 alpha-2 country code.
     */
    @GET("tracks")
    suspend fun getMultipleTracks(
        @Query("ids") ids: String,
        @Query("market") market: String? = null
    ): NetworkResponse<TracksContainer>

    /**
     * Get a list of the songs saved in the current Spotify user's 'Your Music' library.
     *
     * Requires [PermissionScope.UserLibraryRead].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/get-users-saved-tracks">Get User's Saved Tracks</a>
     *
     * @param limit The maximum number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first object to return.
     * @param market An ISO 3166-1 alpha-2 country code.
     */
    @GET("me/tracks")
    suspend fun getUsersSavedTracks(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("market") market: String? = null
    ): NetworkResponse<PaginatedList<SavedTrack>>

} 