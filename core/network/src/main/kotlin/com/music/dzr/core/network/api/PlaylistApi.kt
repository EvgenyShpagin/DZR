package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.AddTracksToPlaylistRequest
import com.music.dzr.core.network.model.ChangePlaylistDetailsRequest
import com.music.dzr.core.network.model.CreatePlaylistRequest
import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.Playlist
import com.music.dzr.core.network.model.PlaylistTrack
import com.music.dzr.core.network.model.RemovePlaylistTracksRequest
import com.music.dzr.core.network.model.SnapshotId
import com.music.dzr.core.network.model.UpdatePlaylistItemsRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API for all things related to playlists.
 *
 * @see <a href="https://developer.spotify.com/documentation/web-api/">Spotify API</a>
 */
interface PlaylistApi {

    /**
     * Get a playlist owned by a Spotify user.
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param market An [ISO 3166-1 alpha-2](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code.
     * @param fields Filters for the query.
     * @return A [Playlist] object.
     */
    @GET("playlists/{playlist_id}")
    suspend fun getPlaylist(
        @Path("playlist_id") playlistId: String,
        @Query("market") market: String? = null,
        @Query("fields") fields: String? = null
    ): NetworkResponse<Playlist>

    /**
     * Change a playlist's name and public/private state. (The user must, of course, own the playlist.)
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param body The request body.
     */
    @PUT("playlists/{playlist_id}")
    suspend fun changePlaylistDetails(
        @Path("playlist_id") playlistId: String,
        @Body body: ChangePlaylistDetailsRequest
    ): NetworkResponse<Unit>

    /**
     * Get full details of the items of a playlist owned by a Spotify user.
     *
     * @param playlistId The [Spotify ID] of the playlist.
     * @param market An [ISO 3166-1 alpha-2](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code.
     * @param fields Filters for the query.
     * @param limit The maximum number of items to return.
     * @param offset The index of the first item to return.
     */
    @GET("playlists/{playlist_id}/tracks")
    suspend fun getPlaylistTracks(
        @Path("playlist_id") playlistId: String,
        @Query("market") market: String? = null,
        @Query("fields") fields: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): NetworkResponse<PaginatedList<PlaylistTrack>>

    /**
     * Either reorder or replace items in a playlist.
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param body The request body.
     * @return A [SnapshotId] object.
     */
    @PUT("playlists/{playlist_id}/tracks")
    suspend fun updatePlaylistItems(
        @Path("playlist_id") playlistId: String,
        @Body body: UpdatePlaylistItemsRequest
    ): NetworkResponse<SnapshotId>

    /**
     * Add one or more items to a user's playlist.
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param body The request body.
     * @return A [SnapshotId] object.
     */
    @POST("playlists/{playlist_id}/tracks")
    suspend fun addTracksToPlaylist(
        @Path("playlist_id") playlistId: String,
        @Body body: AddTracksToPlaylistRequest
    ): NetworkResponse<SnapshotId>

    /**
     * Remove one or more items from a user's playlist.
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param body The request body.
     * @return A [SnapshotId] object.
     */
    @HTTP(method = "DELETE", path = "playlists/{playlist_id}/tracks", hasBody = true)
    suspend fun removePlaylistTracks(
        @Path("playlist_id") playlistId: String,
        @Body body: RemovePlaylistTracksRequest
    ): NetworkResponse<SnapshotId>

    /**
     * Get a list of the playlists owned or followed by the current Spotify user.
     *
     * @param limit The maximum number of items to return.
     * @param offset The index of the first item to return.
     * @return A [PaginatedList] of [Playlist] objects.
     */
    @GET("me/playlists")
    suspend fun getCurrentUserPlaylists(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): NetworkResponse<PaginatedList<Playlist>>

    /**
     * Get a list of the playlists owned or followed by a Spotify user.
     *
     * @param userId The user's Spotify ID.
     * @param limit The maximum number of items to return.
     * @param offset The index of the first item to return.
     * @return A [PaginatedList] of [Playlist] objects.
     */
    @GET("users/{user_id}/playlists")
    suspend fun getUserPlaylists(
        @Path("user_id") userId: String,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): NetworkResponse<PaginatedList<Playlist>>

    /**
     * Create a playlist for a Spotify user. (The playlist will be empty until you add tracks.)
     *
     * @param userId The user's Spotify user ID.
     * @param body The request body.
     * @return The created [Playlist] object.
     */
    @POST("users/{user_id}/playlists")
    suspend fun createPlaylist(
        @Path("user_id") userId: String,
        @Body body: CreatePlaylistRequest
    ): NetworkResponse<Playlist>

} 