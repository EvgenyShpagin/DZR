package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.Artist
import com.music.dzr.core.network.model.CurrentUser
import com.music.dzr.core.network.model.FollowPlaylistRequest
import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.PermissionScope
import com.music.dzr.core.network.model.PublicUser
import com.music.dzr.core.network.model.Track
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * A service for interacting with the Spotify User API.
 * @see <a href="https://developer.spotify.com/documentation/web-api/reference/users">Spotify User API</a>
 */
interface UserApi {
    /**
     * Get detailed profile information about the current user (including the current user's username).
     *
     * Requires:
     * - [PermissionScope.UserReadPrivate],
     * - [PermissionScope.UserReadEmail].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/get-current-users-profile">Get Current User's Profile</a>
     */
    @GET("me")
    suspend fun getCurrentUserProfile(): NetworkResponse<CurrentUser>

    /**
     * Get the current user's top artists based on calculated affinity.
     *
     * Requires [PermissionScope.UserTopRead].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/get-users-top-artists-and-tracks">Get User's Top Items</a>
     *
     * @param timeRange Over what time frame the affinities are computed. Default: medium_term.
     * @param limit The number of entities to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first entity to return.
     */
    @GET("me/top/artists")
    suspend fun getUsersTopArtists(
        @Query("time_range") timeRange: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
    ): NetworkResponse<PaginatedList<Artist>>

    /**
     * Get the current user's top tracks based on calculated affinity.
     *
     * Requires [PermissionScope.UserTopRead].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/get-users-top-artists-and-tracks">Get User's Top Items</a>
     *
     * @param timeRange Over what time frame the affinities are computed. Default: medium_term.
     * @param limit The number of entities to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first entity to return.
     */
    @GET("me/top/tracks")
    suspend fun getUsersTopTracks(
        @Query("time_range") timeRange: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
    ): NetworkResponse<PaginatedList<Track>>

    /**
     * Get public profile information about a Spotify user.
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/get-users-profile">Get User's Profile</a>
     *
     * @param userId The user's Spotify user ID.
     */
    @GET("users/{user_id}")
    suspend fun getUserProfile(@Path("user_id") userId: String): NetworkResponse<PublicUser>

    /**
     * Add the current user as a follower of a playlist.
     *
     * Requires:
     * - [PermissionScope.PlaylistModifyPublic],
     * - [PermissionScope.PlaylistModifyPrivate].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/follow-playlist">Follow Playlist</a>
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param requestBody The request body.
     */
    @PUT("playlists/{playlist_id}/followers")
    suspend fun followPlaylist(
        @Path("playlist_id") playlistId: String,
        @Body requestBody: FollowPlaylistRequest,
    ): NetworkResponse<Unit>

    /**
     * Remove the current user as a follower of a playlist.
     *
     * Requires:
     * - [PermissionScope.PlaylistModifyPublic],
     * - [PermissionScope.PlaylistModifyPrivate].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/unfollow-playlist">Unfollow Playlist</a>
     *
     * @param playlistId The Spotify ID of the playlist.
     */
    @DELETE("playlists/{playlist_id}/followers")
    suspend fun unfollowPlaylist(@Path("playlist_id") playlistId: String): NetworkResponse<Unit>

}