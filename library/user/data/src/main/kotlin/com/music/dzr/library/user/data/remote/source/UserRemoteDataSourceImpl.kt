package com.music.dzr.library.user.data.remote.source

import com.music.dzr.core.network.dto.Artist
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.PlaylistFollowDetails
import com.music.dzr.core.network.dto.PublicUser
import com.music.dzr.core.network.dto.Track
import com.music.dzr.library.user.data.remote.api.UserApi
import com.music.dzr.library.user.data.remote.dto.CurrentUser
import com.music.dzr.library.user.data.remote.dto.FollowedArtists
import com.music.dzr.library.user.data.remote.dto.TimeRange

internal class UserRemoteDataSourceImpl(
    private val userApi: UserApi
) : UserRemoteDataSource {

    override suspend fun getCurrentUserProfile(): NetworkResponse<CurrentUser> {
        return userApi.getCurrentUserProfile()
    }

    override suspend fun getUserTopArtists(
        timeRange: TimeRange?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<Artist>> {
        return userApi.getUserTopArtists(
            timeRange = timeRange,
            limit = limit,
            offset = offset
        )
    }

    override suspend fun getUserTopTracks(
        timeRange: TimeRange?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<Track>> {
        return userApi.getUserTopTracks(
            timeRange = timeRange,
            limit = limit,
            offset = offset
        )
    }

    override suspend fun getUserProfile(userId: String): NetworkResponse<PublicUser> {
        return userApi.getUserProfile(userId = userId)
    }

    override suspend fun followPlaylist(
        playlistId: String,
        asPublic: Boolean
    ): NetworkResponse<Unit> {
        return userApi.followPlaylist(
            playlistId = playlistId,
            PlaylistFollowDetails(asPublic)
        )
    }

    override suspend fun unfollowPlaylist(playlistId: String): NetworkResponse<Unit> {
        return userApi.unfollowPlaylist(playlistId = playlistId)
    }

    override suspend fun getFollowedArtists(
        limit: Int?,
        after: String?
    ): NetworkResponse<FollowedArtists> {
        return userApi.getFollowedArtists(limit = limit, after = after)
    }

    override suspend fun followArtists(ids: List<String>): NetworkResponse<Unit> {
        val csv = ids.joinToString(",")
        return userApi.followArtists(ids = csv)
    }

    override suspend fun followUsers(ids: List<String>): NetworkResponse<Unit> {
        val csv = ids.joinToString(",")
        return userApi.followUsers(ids = csv)
    }

    override suspend fun unfollowArtists(ids: List<String>): NetworkResponse<Unit> {
        val csv = ids.joinToString(",")
        return userApi.unfollowArtists(ids = csv)
    }

    override suspend fun unfollowUsers(ids: List<String>): NetworkResponse<Unit> {
        val csv = ids.joinToString(",")
        return userApi.unfollowUsers(ids = csv)
    }

    override suspend fun checkIfUserFollowsArtists(
        ids: List<String>
    ): NetworkResponse<List<Boolean>> {
        val csv = ids.joinToString(",")
        return userApi.checkIfUserFollowsArtists(ids = csv)
    }

    override suspend fun checkIfUserFollowsUsers(
        ids: List<String>
    ): NetworkResponse<List<Boolean>> {
        val csv = ids.joinToString(",")
        return userApi.checkIfUserFollowsUsers(ids = csv)
    }

    override suspend fun checkIfUsersFollowPlaylist(
        playlistId: String
    ): NetworkResponse<Boolean> {
        val response = userApi.checkIfUsersFollowPlaylist(playlistId = playlistId)
        return NetworkResponse(data = response.data?.single(), error = response.error)
    }
}
