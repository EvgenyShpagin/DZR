package com.music.dzr.library.user.data.remote.source

import com.music.dzr.core.data.test.HasForcedError
import com.music.dzr.core.data.test.runUnlessForcedError
import com.music.dzr.core.network.dto.Artist
import com.music.dzr.core.network.dto.Cursor
import com.music.dzr.core.network.dto.CursorPaginatedList
import com.music.dzr.core.network.dto.ExternalUrls
import com.music.dzr.core.network.dto.Followers
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.PublicUser
import com.music.dzr.core.network.dto.Track
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.library.user.data.remote.dto.CurrentUser
import com.music.dzr.library.user.data.remote.dto.ExplicitContent
import com.music.dzr.library.user.data.remote.dto.FollowedArtists
import com.music.dzr.library.user.data.remote.dto.TimeRange

/**
 * Configurable in-memory test implementation of [UserRemoteDataSource] with default data.
 *
 * State is set via constructor or direct property assignment. Set [forcedError] to return failures.
 *
 * Not thread-safe.
 */
internal class TestUserRemoteDataSource(
    var publicUser: PublicUser = defaultPublicUser,
    var currentUser: CurrentUser = defaultCurrentUser,
    var userTopArtists: PaginatedList<Artist> = defaultUserTopArtists,
    var userTopTracks: PaginatedList<Track> = defaultUserTopTracks
) : UserRemoteDataSource, HasForcedError<NetworkError> {

    override var forcedError: NetworkError? = null

    override suspend fun getCurrentUserProfile() = runUnlessForcedError { currentUser }

    override suspend fun getUserTopArtists(
        timeRange: TimeRange?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<Artist>> = runUnlessForcedError { userTopArtists }

    override suspend fun getUserTopTracks(
        timeRange: TimeRange?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<Track>> = runUnlessForcedError { userTopTracks }

    override suspend fun getUserProfile(
        userId: String
    ): NetworkResponse<PublicUser> = runUnlessForcedError { publicUser }

    override suspend fun followPlaylist(
        playlistId: String,
        asPublic: Boolean
    ): NetworkResponse<Unit> = runUnlessForcedError { }

    override suspend fun unfollowPlaylist(
        playlistId: String
    ): NetworkResponse<Unit> = runUnlessForcedError { }

    override suspend fun getFollowedArtists(
        limit: Int?,
        after: String?
    ): NetworkResponse<FollowedArtists> = runUnlessForcedError {
        val paginatedList = CursorPaginatedList<Artist>(
            items = emptyList(),
            href = "",
            limit = 0,
            next = null,
            total = 0,
            cursors = Cursor("", "")
        )
        FollowedArtists(paginatedList)
    }

    override suspend fun followArtists(ids: List<String>) = runUnlessForcedError { }
    override suspend fun followUsers(ids: List<String>) = runUnlessForcedError { }
    override suspend fun unfollowArtists(ids: List<String>) = runUnlessForcedError { }
    override suspend fun unfollowUsers(ids: List<String>) = runUnlessForcedError { }

    override suspend fun checkIfUserFollowsArtists(
        ids: List<String>
    ): NetworkResponse<List<Boolean>> = runUnlessForcedError {
        ids.map { false }
    }

    override suspend fun checkIfUserFollowsUsers(
        ids: List<String>
    ): NetworkResponse<List<Boolean>> = runUnlessForcedError {
        ids.map { false }
    }

    override suspend fun checkIfUsersFollowPlaylist(
        playlistId: String
    ): NetworkResponse<Boolean> = runUnlessForcedError { false }
}

private val defaultCurrentUser = CurrentUser(
    id = "test_user",
    displayName = "Test User",
    email = "test@example.com",
    country = "US",
    followers = Followers(total = 10, href = ""),
    images = emptyList(),
    externalUrls = ExternalUrls(spotify = ""),
    explicitContent = ExplicitContent(filterEnabled = false, filterLocked = false),
    product = null,
    href = "",
    type = "user",
    uri = "uri",
)

private val defaultPublicUser = PublicUser(
    id = "public_user_123",
    displayName = "Public User",
    followers = Followers(total = 5, href = ""),
    images = emptyList(),
    externalUrls = ExternalUrls(spotify = ""),
    href = "",
    type = "user",
    uri = "uri",
)

private val defaultUserTopArtists = PaginatedList("", emptyList<Artist>(), 0, "", 0, "", 0)
private val defaultUserTopTracks = PaginatedList("", emptyList<Track>(), 0, "", 0, "", 0)