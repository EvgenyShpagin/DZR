package com.music.dzr.library.user.domain.repository

import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.DetailedArtist
import com.music.dzr.core.model.DetailedTrack
import com.music.dzr.core.pagination.Page
import com.music.dzr.core.model.User
import com.music.dzr.core.result.Result
import com.music.dzr.library.user.domain.model.CurrentUser
import com.music.dzr.library.user.domain.model.TimeRange

/**
 * Provides access to user-related data, such as profiles and follow status.
 *
 * All methods in this repository return a [com.music.dzr.core.result.Result] and can fail with:
 * - [com.music.dzr.core.error.ConnectivityError] for network connection issues.
 * - [com.music.dzr.core.error.NetworkError] for API-related problems (e.g., authorization, server errors).
 * Specific domain errors are documented on a per-method basis.
 */
interface UserRepository {

    /**
     * Get detailed profile information about the current user.
     */
    suspend fun getCurrentUserProfile(): Result<CurrentUser, AppError>

    /**
     * Get the current user's top artists.
     */
    suspend fun getUserTopArtists(
        timeRange: TimeRange? = null,
        limit: Int? = null,
        offset: Int? = null
    ): Result<Page<DetailedArtist>, AppError>

    /**
     * Get the current user's top tracks.
     */
    suspend fun getUserTopTracks(
        timeRange: TimeRange? = null,
        limit: Int? = null,
        offset: Int? = null
    ): Result<Page<DetailedTrack>, AppError>

    /**
     * Get public profile information about a Spotify user.
     */
    suspend fun getUserProfile(userId: String): Result<User, AppError>

    /**
     * Follow a playlist.
     */
    suspend fun followPlaylist(
        playlistId: String,
        asPublic: Boolean = true
    ): Result<Unit, AppError>

    /**
     * Unfollow a playlist.
     */
    suspend fun unfollowPlaylist(playlistId: String): Result<Unit, AppError>

    /**
     * Get the current user's followed artists.
     */
    suspend fun getFollowedArtists(
        limit: Int? = null,
        after: String? = null
    ): Result<Page<DetailedArtist>, AppError>

    /**
     * Follow one or more artists.
     */
    suspend fun followArtists(ids: List<String>): Result<Unit, AppError>

    /**
     * Follow one or more users.
     */
    suspend fun followUsers(ids: List<String>): Result<Unit, AppError>

    /**
     * Unfollow one or more artists.
     */
    suspend fun unfollowArtists(ids: List<String>): Result<Unit, AppError>

    /**
     * Unfollow one or more users.
     */
    suspend fun unfollowUsers(ids: List<String>): Result<Unit, AppError>

    /**
     * Check if the current user follows one or more artists.
     */
    suspend fun checkIfUserFollowsArtists(ids: List<String>): Result<List<Boolean>, AppError>

    /**
     * Check if the current user follows one or more users.
     */
    suspend fun checkIfUserFollowsUsers(ids: List<String>): Result<List<Boolean>, AppError>

    /**
     * Check if the current user follows a playlist.
     */
    suspend fun checkIfUsersFollowPlaylist(playlistId: String): Result<Boolean, AppError>
}
