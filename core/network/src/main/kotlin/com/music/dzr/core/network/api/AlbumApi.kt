package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.album.Album
import com.music.dzr.core.network.model.album.Albums
import com.music.dzr.core.network.model.album.NewReleases
import com.music.dzr.core.network.model.album.SavedAlbum
import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.album.AlbumTrack
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * A service for interacting with the Spotify Album API.
 * @see <a href="https://developer.spotify.com/documentation/web-api/">Spotify API</a>
 */
interface AlbumApi {

    /**
     * Retrieves detailed information about a single album from the Spotify catalog.
     *
     * @param id     The Spotify ID of the album to retrieve.
     * @param market An ISO 3166-1 alpha-2 country code to filter track availability.
     */
    @GET("albums/{id}")
    suspend fun getAlbum(
        @Path("id") id: String,
        @Query("market") market: String? = null
    ): NetworkResponse<Album>

    /**
     * Retrieves detailed information for multiple albums in a single request.
     *
     * @param ids    A comma-separated list of Spotify album IDs (maximum of 20).
     * @param market An ISO 3166-1 alpha-2 country code to filter track availability.
     */
    @GET("albums")
    suspend fun getMultipleAlbums(
        @Query("ids") ids: String,
        @Query("market") market: String? = null
    ): NetworkResponse<Albums>

    /**
     * Retrieves a paginated list of tracks for a given album.
     *
     * @param id     The Spotify ID of the album whose tracks are to be retrieved.
     * @param market An ISO 3166-1 alpha-2 country code to filter track availability.
     * @param limit  The maximum number of tracks to return (default and maximum value as defined by Spotify).
     * @param offset The index of the first track to return (used for pagination).
     */
    @GET("albums/{id}/tracks")
    suspend fun getAlbumTracks(
        @Path("id") id: String,
        @Query("market") market: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): NetworkResponse<PaginatedList<AlbumTrack>>

    /**
     * Retrieves a paginated list of albums saved in the current user’s library.
     *
     * Requires [PermissionScope.UserLibraryRead]
     *
     * @param limit  The maximum number of items to return (default and maximum value as defined by Spotify).
     * @param offset The index of the first item to return (used for pagination).
     * @param market An ISO 3166-1 alpha-2 country code to filter album availability.
     */
    @GET("me/albums")
    suspend fun getUsersSavedAlbums(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("market") market: String? = null
    ): NetworkResponse<PaginatedList<SavedAlbum>>

    /**
     * Save one or more albums to the current user’s library.
     *
     * Requires [PermissionScope.UserLibraryModify]
     *
     * @param ids Required query param: ids (comma-separated IDs, max 20)
     */
    @PUT("me/albums")
    suspend fun saveAlbumsForUser(
        @Query("ids") ids: String
    ): NetworkResponse<Unit>

    /**
     * Save one or more albums to the current user’s library.
     *
     * Requires [PermissionScope.UserLibraryModify]
     *
     * @param ids List of ids (max 50)
     */
    @PUT("me/albums")
    suspend fun saveAlbumsForUser(
        @Body ids: List<String>
    ): NetworkResponse<Unit>

    /**
     * Remove one or more albums from the current user’s library.
     *
     * Requires [PermissionScope.UserLibraryModify]
     *
     * @param ids An id list of albums (comma-separated IDs, max 20)
     */
    @DELETE("me/albums")
    suspend fun removeAlbumsForUser(
        @Query("ids") ids: String
    ): NetworkResponse<Unit>

    /**
     * Removes one or more albums from the current user’s library.
     *
     * Requires [PermissionScope.UserLibraryModify]
     *
     * @param ids Id list of albums (comma-separated IDs, max 20)
     * @param ids List of ids in format (max 50)
     */
    @HTTP(method = "DELETE", path = "me/albums", hasBody = true)
    suspend fun removeAlbumsForUser(
        @Body ids: List<String>
    ): NetworkResponse<Unit>

    /**
     * Check if one or more albums are already saved in the current user’s library.
     *
     * Requires [PermissionScope.UserLibraryRead]
     *
     * @param ids An id list of albums (comma-separated IDs, max 20)
     */
    @GET("me/albums/contains")
    suspend fun checkUsersSavedAlbums(
        @Query("ids") ids: String
    ): NetworkResponse<List<Boolean>>

    /**
     * Get a list of new album releases featured in Spotify.
     *
     * @param limit The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0 (the first item).
     */
    @GET("browse/new-releases")
    suspend fun getNewReleases(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): NetworkResponse<NewReleases>
}