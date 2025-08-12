package com.music.dzr.library.track.data.remote.api

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Track
import com.music.dzr.core.network.dto.Tracks
import com.music.dzr.library.track.data.remote.dto.SavedTrack
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * A service for interacting with the Spotify Track API.
 * @see <a href="https://developer.spotify.com/documentation/web-api/reference/tracks">Spotify Track API</a>
 */
internal interface TrackApi {
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
    ): NetworkResponse<Tracks>

    /**
     * Get a list of the songs saved in the current Spotify user's 'Your Music' library.
     *
     * Requires permission `UserLibraryRead`.
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

    /**
     * Save one or more tracks to the current user's 'Your Music' library.
     *
     * Requires permission `UserLibraryModify`.
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/save-tracks-user">Save Tracks for Current User</a>
     *
     * @param ids A comma-separated list of the Spotify IDs. Maximum: 50.
     */
    @PUT("me/tracks")
    suspend fun saveTracksForUser(
        @Query("ids") ids: String
    ): NetworkResponse<Unit>

    /**
     * Save one or more tracks to the current user's 'Your Music' library.
     *
     * Requires permission `UserLibraryModify`.
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/save-tracks-user">Save Tracks for Current User</a>
     *
     * @param ids A list of the Spotify IDs. Maximum: 50.
     */
    @PUT("me/tracks")
    suspend fun saveTracksForUser(
        @Body ids: List<String>
    ): NetworkResponse<Unit>

    /**
     * Remove one or more tracks from the current user's 'Your Music' library.
     *
     * Requires permission `UserLibraryModify`.
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/remove-tracks-user">Remove User's Saved Tracks</a>
     *
     * @param ids A comma-separated list of the Spotify IDs. Maximum: 50.
     */
    @DELETE("me/tracks")
    suspend fun removeTracksForUser(
        @Query("ids") ids: String
    ): NetworkResponse<Unit>

    /**
     * Remove one or more tracks from the current user's 'Your Music' library.
     *
     * Requires permission `UserLibraryModify`.
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/remove-tracks-user">Remove User's Saved Tracks</a>
     *
     * @param ids A list of the Spotify IDs. Maximum: 50.
     */
    @HTTP(method = "DELETE", path = "me/tracks", hasBody = true)
    suspend fun removeTracksForUser(
        @Body ids: List<String>
    ): NetworkResponse<Unit>

    /**
     * Check if one or more tracks is already saved in the current Spotify user's 'Your Music' library.
     *
     * Requires permission `UserLibraryRead`.
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/check-users-saved-tracks">Check User's Saved Tracks</a>
     *
     * @param ids A comma-separated list of the Spotify IDs for the tracks. Maximum: 50.
     */
    @GET("me/tracks/contains")
    suspend fun checkUsersSavedTracks(
        @Query("ids") ids: String
    ): NetworkResponse<List<Boolean>>
} 