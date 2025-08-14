package com.music.dzr.library.playlist.data.remote.api

import com.music.dzr.core.network.dto.Image
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Playlist
import com.music.dzr.core.network.dto.PlaylistTrack
import com.music.dzr.core.network.dto.PlaylistWithPaginatedTracks
import com.music.dzr.core.network.dto.PlaylistWithTracks
import com.music.dzr.core.network.dto.PlaylistWithTracksInfo
import com.music.dzr.core.network.dto.SnapshotId
import com.music.dzr.library.playlist.data.remote.dto.NewPlaylistDetails
import com.music.dzr.library.playlist.data.remote.dto.PlaylistDetailsUpdate
import com.music.dzr.library.playlist.data.remote.dto.PlaylistFields
import com.music.dzr.library.playlist.data.remote.dto.PlaylistItemsUpdate
import com.music.dzr.library.playlist.data.remote.dto.TrackAdditions
import com.music.dzr.library.playlist.data.remote.dto.TrackRemovals
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API for all things related to playlists.
 *
 * @see <a href="https://developer.spotify.com/documentation/web-api/">Spotify API</a>
 */
internal interface PlaylistApi {

    /**
     * Get a playlist owned by a Spotify user.
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param market An [ISO 3166-1 alpha-2](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code.
     * @param fields Filters for the query.
     * @return A [Playlist] object.
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/get-playlist">Spotify API</a>
     */
    @GET("playlists/{playlist_id}")
    suspend fun getPlaylist(
        @Path("playlist_id") playlistId: String,
        @Query("market") market: String? = null,
        @Query("fields") fields: PlaylistFields? = null
    ): NetworkResponse<PlaylistWithPaginatedTracks>

    /**
     * Change a playlist's name and public/private state. (The user must, of course, own the playlist.)
     *
     * Requires permissions:
     * - `PlaylistModifyPublic`
     * - `PlaylistModifyPrivate`
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param update The request body.
     */
    @PUT("playlists/{playlist_id}")
    suspend fun changePlaylistDetails(
        @Path("playlist_id") playlistId: String,
        @Body update: PlaylistDetailsUpdate
    ): NetworkResponse<Unit>

    /**
     * Get full details of the items of a playlist owned by a Spotify user.
     *
     * Requires permission `PlaylistReadPrivate`
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
     * Requires permissions:
     * - `PlaylistModifyPublic`
     * - `PlaylistModifyPrivate`
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param update The request body.
     * @return A [SnapshotId] object.
     */
    @PUT("playlists/{playlist_id}/tracks")
    suspend fun updatePlaylistTracks(
        @Path("playlist_id") playlistId: String,
        @Body update: PlaylistItemsUpdate
    ): NetworkResponse<SnapshotId>

    /**
     * Add one or more items to a user's playlist.
     *
     * Requires permissions:
     * - `PlaylistModifyPublic`
     * - `PlaylistModifyPrivate`
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param body The request body.
     * @return A [SnapshotId] object.
     */
    @POST("playlists/{playlist_id}/tracks")
    suspend fun addTracksToPlaylist(
        @Path("playlist_id") playlistId: String,
        @Body body: TrackAdditions
    ): NetworkResponse<SnapshotId>

    /**
     * Remove one or more items from a user's playlist.
     *
     * Requires permissions:
     * - `PlaylistModifyPublic`
     * - `PlaylistModifyPrivate`
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param body The request body.
     * @return A [SnapshotId] object.
     */
    @HTTP(method = "DELETE", path = "playlists/{playlist_id}/tracks", hasBody = true)
    suspend fun removePlaylistTracks(
        @Path("playlist_id") playlistId: String,
        @Body body: TrackRemovals
    ): NetworkResponse<SnapshotId>

    /**
     * Get a list of the playlists owned or followed by the current Spotify user.
     *
     * Requires permission `PlaylistReadPrivate`
     *
     * @param limit The maximum number of items to return.
     * @param offset The index of the first item to return.
     * @return A [PaginatedList] of [Playlist] objects.
     */
    @GET("me/playlists")
    suspend fun getCurrentUserPlaylists(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): NetworkResponse<PaginatedList<PlaylistWithTracksInfo>>

    /**
     * Get a list of the playlists owned or followed by a Spotify user.
     *
     * Requires permissions:
     * - `PlaylistReadPrivate`
     * - `PlaylistReadCollaborative`
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
    ): NetworkResponse<PaginatedList<PlaylistWithTracks>>

    /**
     * Create a playlist for a Spotify user. (The playlist will be empty until you add tracks.)
     *
     * Requires permissions:
     * - `PlaylistModifyPublic`
     * - `PlaylistModifyPrivate`
     *
     * @param userId The user's Spotify user ID.
     * @param details The request body.
     * @return The created [Playlist] object.
     */
    @POST("users/{user_id}/playlists")
    suspend fun createPlaylist(
        @Path("user_id") userId: String,
        @Body details: NewPlaylistDetails
    ): NetworkResponse<PlaylistWithPaginatedTracks>

    /**
     * Get the current cover image for a playlist.
     *
     * @param playlistId The Spotify ID of the playlist.
     * @return A list of [Image] objects.
     */
    @GET("playlists/{playlist_id}/images")
    suspend fun getPlaylistCoverImage(
        @Path("playlist_id") playlistId: String
    ): NetworkResponse<List<Image>>

    /**
     * Replace the image used to represent a specific playlist.
     *
     * Requires permissions:
     * - `UgcImageUpload`
     * - `PlaylistModifyPublic`
     * - `PlaylistModifyPrivate`
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param encodedImageData The image data, base64-encoded.
     */
    @PUT("playlists/{playlist_id}/images")
    @Headers("Content-Type: image/jpeg")
    suspend fun uploadCustomPlaylistCover(
        @Path("playlist_id") playlistId: String,
        @Body encodedImageData: RequestBody
    ): NetworkResponse<Unit>
}