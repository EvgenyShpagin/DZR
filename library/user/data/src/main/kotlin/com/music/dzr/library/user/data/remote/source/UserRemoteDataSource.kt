package com.music.dzr.library.user.data.remote.source

import com.music.dzr.core.network.dto.*
import com.music.dzr.library.user.data.remote.api.UserApi
import com.music.dzr.library.user.data.remote.dto.CurrentUser
import com.music.dzr.library.user.data.remote.dto.FollowedArtists
import com.music.dzr.library.user.data.remote.dto.TimeRange

/**
 * Remote data source for user-related operations.
 * Thin wrapper around [UserApi] with convenient method signatures and parameter preparation.
 */
internal interface UserRemoteDataSource {

    /**
     * Get detailed profile information about the current user.
     */
    suspend fun getCurrentUserProfile(): NetworkResponse<CurrentUser>

    /**
     * Get the current user's top artists.
     */
    suspend fun getUsersTopArtists(
        timeRange: TimeRange? = null,
        limit: Int? = null,
        offset: Int? = null
    ): NetworkResponse<PaginatedList<Artist>>

    /**
     * Get the current user's top tracks.
     */
    suspend fun getUsersTopTracks(
        timeRange: TimeRange? = null,
        limit: Int? = null,
        offset: Int? = null
    ): NetworkResponse<PaginatedList<Track>>

    /**
     * Get public profile information about a Spotify user.
     */
    suspend fun getUserProfile(userId: String): NetworkResponse<PublicUser>

    /**
     * Follow a playlist with optional public/private visibility.
     */
    suspend fun followPlaylist(
        playlistId: String,
        asPublic: Boolean = true
    ): NetworkResponse<Unit>

    /**
     * Unfollow a playlist.
     */
    suspend fun unfollowPlaylist(playlistId: String): NetworkResponse<Unit>

    /**
     * Get the current user's followed artists (cursor-based pagination).
     */
    suspend fun getFollowedArtists(
        limit: Int? = null,
        after: String? = null
    ): NetworkResponse<FollowedArtists>

    /**
     * Follow one or more artists.
     */
    suspend fun followArtists(ids: List<String>): NetworkResponse<Unit>

    /**
     * Follow one or more users.
     */
    suspend fun followUsers(ids: List<String>): NetworkResponse<Unit>

    /**
     * Unfollow one or more artists.
     */
    suspend fun unfollowArtists(ids: List<String>): NetworkResponse<Unit>

    /**
     * Unfollow one or more users.
     */
    suspend fun unfollowUsers(ids: List<String>): NetworkResponse<Unit>

    /**
     * Check whether the current user follows the specified artists.
     */
    suspend fun checkIfUserFollowsArtists(ids: List<String>): NetworkResponse<List<Boolean>>

    /**
     * Check whether the current user follows the specified users.
     */
    suspend fun checkIfUserFollowsUsers(ids: List<String>): NetworkResponse<List<Boolean>>

    /**
     * Check whether one or more users follow a playlist.
     */
    suspend fun checkIfUsersFollowPlaylist(playlistId: String): NetworkResponse<List<Boolean>>
}


