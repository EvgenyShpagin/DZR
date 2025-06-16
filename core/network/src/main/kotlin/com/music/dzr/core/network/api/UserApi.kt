package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.CurrentUser
import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.PermissionScope
import retrofit2.http.GET

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

}