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
internal class UserRemoteDataSource(private val userApi: UserApi) {

    /**
     * Get detailed profile information about the current user.
     */
    suspend fun getCurrentUserProfile(): NetworkResponse<CurrentUser> {
        return userApi.getCurrentUserProfile()
    }

    /**
     * Get the current user's top artists.
     */
    suspend fun getUsersTopArtists(
        timeRange: TimeRange? = null,
        limit: Int? = null,
        offset: Int? = null
    ): NetworkResponse<PaginatedList<Artist>> {
        return userApi.getUsersTopArtists(timeRange = timeRange, limit = limit, offset = offset)
    }

    /**
     * Get the current user's top tracks.
     */
    suspend fun getUsersTopTracks(
        timeRange: TimeRange? = null,
        limit: Int? = null,
        offset: Int? = null
    ): NetworkResponse<PaginatedList<Track>> {
        return userApi.getUsersTopTracks(timeRange = timeRange, limit = limit, offset = offset)
    }

    /**
     * Get public profile information about a Spotify user.
     */
    suspend fun getUserProfile(userId: String): NetworkResponse<PublicUser> {
        return userApi.getUserProfile(userId = userId)
    }

    /**
     * Follow a playlist with optional public/private visibility.
     */
    suspend fun followPlaylist(
        playlistId: String,
        asPublic: Boolean = true
    ): NetworkResponse<Unit> {
        return userApi.followPlaylist(
            playlistId = playlistId,
            PlaylistFollowDetails(asPublic)
        )
    }

    /**
     * Unfollow a playlist.
     */
    suspend fun unfollowPlaylist(playlistId: String): NetworkResponse<Unit> {
        return userApi.unfollowPlaylist(playlistId = playlistId)
    }

    /**
     * Get the current user's followed artists (cursor-based pagination).
     */
    suspend fun getFollowedArtists(
        limit: Int? = null,
        after: String? = null
    ): NetworkResponse<FollowedArtists> {
        return userApi.getFollowedArtists(limit = limit, after = after)
    }

    /**
     * Follow one or more artists.
     */
    suspend fun followArtists(ids: List<String>): NetworkResponse<Unit> {
        val csv = ids.joinToString(",")
        return userApi.followArtists(ids = csv)
    }

    /**
     * Follow one or more users.
     */
    suspend fun followUsers(ids: List<String>): NetworkResponse<Unit> {
        val csv = ids.joinToString(",")
        return userApi.followUsers(ids = csv)
    }

    /**
     * Unfollow one or more artists.
     */
    suspend fun unfollowArtists(ids: List<String>): NetworkResponse<Unit> {
        val csv = ids.joinToString(",")
        return userApi.unfollowArtists(ids = csv)
    }

    /**
     * Unfollow one or more users.
     */
    suspend fun unfollowUsers(ids: List<String>): NetworkResponse<Unit> {
        val csv = ids.joinToString(",")
        return userApi.unfollowUsers(ids = csv)
    }

    /**
     * Check whether the current user follows the specified artists.
     */
    suspend fun checkIfUserFollowsArtists(ids: List<String>): NetworkResponse<List<Boolean>> {
        val csv = ids.joinToString(",")
        return userApi.checkIfUserFollowsArtists(ids = csv)
    }

    /**
     * Check whether the current user follows the specified users.
     */
    suspend fun checkIfUserFollowsUsers(ids: List<String>): NetworkResponse<List<Boolean>> {
        val csv = ids.joinToString(",")
        return userApi.checkIfUserFollowsUsers(ids = csv)
    }

    /**
     * Check whether one or more users follow a playlist.
     */
    suspend fun checkIfUsersFollowPlaylist(playlistId: String): NetworkResponse<List<Boolean>> {
        return userApi.checkIfUsersFollowPlaylist(playlistId = playlistId)
    }
}


