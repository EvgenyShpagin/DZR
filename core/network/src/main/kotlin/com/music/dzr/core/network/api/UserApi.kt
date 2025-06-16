package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.Artist
import com.music.dzr.core.network.model.CurrentUser
import com.music.dzr.core.network.model.FollowPlaylistRequest
import com.music.dzr.core.network.model.FollowedArtistsContainer
import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.PermissionScope
import com.music.dzr.core.network.model.PublicUser
import com.music.dzr.core.network.model.TimeRange
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
     * @param timeRange Over what time frame the affinities are computed. Default: [TimeRange.MediumTerm].
     * @param limit The number of entities to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first entity to return.
     */
    @GET("me/top/artists")
    suspend fun getUsersTopArtists(
        @Query("time_range") timeRange: TimeRange? = null,
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
     * @param timeRange Over what time frame the affinities are computed. Default: [TimeRange.MediumTerm]
     * @param limit The number of entities to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first entity to return. Default: 0
     */
    @GET("me/top/tracks")
    suspend fun getUsersTopTracks(
        @Query("time_range") timeRange: TimeRange? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
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

    /**
     * Get the current user's followed artists.
     *
     * Requires [PermissionScope.UserFollowRead].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/get-followed">Get Followed Artists</a>
     *
     * @param limit The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param after The last artist ID retrieved from the previous request.
     */
    @GET("me/following?type=artist")
    suspend fun getFollowedArtists(
        @Query("limit") limit: Int? = null,
        @Query("after") after: String? = null,
    ): NetworkResponse<FollowedArtistsContainer>

    /**
     * Add the current user as a follower of one or more artists.
     *
     * Requires [PermissionScope.UserFollowModify].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/follow-artists-users">Follow Artists or Users</a>
     *
     * @param ids A comma-separated list of the artist IDs to follow. Maximum 50.
     */
    @PUT("me/following?type=artist")
    suspend fun followArtists(@Query("ids") ids: String): NetworkResponse<Unit>

    /**
     * Add the current user as a follower of one or more users.
     *
     * Requires [PermissionScope.UserFollowModify].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/follow-artists-users">Follow Artists or Users</a>
     *
     * @param ids A comma-separated list of the user IDs to follow. Maximum 50.
     */
    @PUT("me/following?type=user")
    suspend fun followUsers(@Query("ids") ids: String): NetworkResponse<Unit>

    /**
     * Remove the current user as a follower of one or more artists.
     *
     * Requires [PermissionScope.UserFollowModify].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/unfollow-artists-users">Unfollow Artists or Users</a>
     *
     * @param ids A comma-separated list of the artist IDs to unfollow. Maximum 50.
     */
    @DELETE("me/following?type=artist")
    suspend fun unfollowArtists(@Query("ids") ids: String): NetworkResponse<Unit>

    /**
     * Remove the current user as a follower of one or more users.
     *
     * Requires [PermissionScope.UserFollowModify].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/unfollow-artists-users">Unfollow Artists or Users</a>
     *
     * @param ids A comma-separated list of the user IDs to unfollow. Maximum 50.
     */
    @DELETE("me/following?type=user")
    suspend fun unfollowUsers(@Query("ids") ids: String): NetworkResponse<Unit>

    /**
     * Check to see if the current user is following one or more artists.
     *
     * Requires [PermissionScope.UserFollowRead].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/check-current-user-follows">Check If User Follows Artists or Users</a>
     *
     * @param ids A comma-separated list of the artist IDs to check. Maximum 50.
     */
    @GET("me/following/contains?type=artist")
    suspend fun checkIfUserFollowsArtists(@Query("ids") ids: String): NetworkResponse<List<Boolean>>

    /**
     * Check to see if the current user is following one or more users.
     *
     * Requires [PermissionScope.UserFollowRead].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/check-current-user-follows">Check If User Follows Artists or Users</a>
     *
     * @param ids A comma-separated list of the user IDs to check. Maximum 50.
     */
    @GET("me/following/contains?type=user")
    suspend fun checkIfUserFollowsUsers(@Query("ids") ids: String): NetworkResponse<List<Boolean>>

    /**
     * Check to see if one or more Spotify users are following a specified playlist.
     *
     * Requires [PermissionScope.PlaylistReadPrivate].
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/check-if-user-follows-playlist">Check if Users Follow Playlist</a>
     *
     * @param playlistId The Spotify ID of the playlist.
     */
    @GET("playlists/{playlist_id}/followers/contains")
    suspend fun checkIfUsersFollowPlaylist(
        @Path("playlist_id") playlistId: String
    ): NetworkResponse<List<Boolean>>
}