package com.music.dzr.library.user.data.remote.source

import com.music.dzr.core.data.test.HasForcedNetworkError
import com.music.dzr.core.data.test.respond
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
 * In-memory Fake implementation of [UserRemoteDataSource].
 *
 * Mirrors the contract of the real remote source but keeps all state in memory so tests can
 * deterministically set up scenarios and observe effects without network.
 */
internal class FakeUserRemoteDataSource : UserRemoteDataSource, HasForcedNetworkError {

    override var forcedError: NetworkError? = null
    var publicUser = createFakePublicUser()
    var currentUser = createFakeCurrentUser()
    var userTopArtists = PaginatedList("", emptyList<Artist>(), 0, "", 0, "", 0)
    var userTopTracks = PaginatedList("", emptyList<Track>(), 0, "", 0, "", 0)

    override suspend fun getCurrentUserProfile(): NetworkResponse<CurrentUser> = respond {
        currentUser
    }

    override suspend fun getUserTopArtists(
        timeRange: TimeRange?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<Artist>> = respond { userTopArtists }

    override suspend fun getUserTopTracks(
        timeRange: TimeRange?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<Track>> = respond { userTopTracks }

    override suspend fun getUserProfile(
        userId: String
    ): NetworkResponse<PublicUser> = respond { publicUser }

    override suspend fun followPlaylist(
        playlistId: String,
        asPublic: Boolean
    ): NetworkResponse<Unit> = respond { }

    override suspend fun unfollowPlaylist(
        playlistId: String
    ): NetworkResponse<Unit> = respond { }

    override suspend fun getFollowedArtists(
        limit: Int?,
        after: String?
    ): NetworkResponse<FollowedArtists> = respond {
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

    override suspend fun followArtists(ids: List<String>) = respond { }
    override suspend fun followUsers(ids: List<String>) = respond { }
    override suspend fun unfollowArtists(ids: List<String>) = respond { }
    override suspend fun unfollowUsers(ids: List<String>) = respond { }

    override suspend fun checkIfUserFollowsArtists(
        ids: List<String>
    ): NetworkResponse<List<Boolean>> = respond {
        ids.map { false }
    }

    override suspend fun checkIfUserFollowsUsers(
        ids: List<String>
    ): NetworkResponse<List<Boolean>> = respond {
        ids.map { false }
    }

    override suspend fun checkIfUsersFollowPlaylist(
        playlistId: String
    ): NetworkResponse<Boolean> = respond { false }
}

private fun createFakeCurrentUser() = CurrentUser(
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

private fun createFakePublicUser() = PublicUser(
    id = "public_user_123",
    displayName = "Public User",
    followers = Followers(total = 5, href = ""),
    images = emptyList(),
    externalUrls = ExternalUrls(spotify = ""),
    href = "",
    type = "user",
    uri = "uri",
)
