package com.music.dzr.library.user.data.remote.source

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.PlaylistFollowDetails
import com.music.dzr.library.user.data.remote.api.UserApi
import com.music.dzr.library.user.data.remote.dto.FollowedArtists
import com.music.dzr.library.user.data.remote.dto.TimeRange
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame

class UserRemoteDataSourceImplTest {

    private lateinit var api: UserApi
    private lateinit var dataSource: UserRemoteDataSourceImpl

    @BeforeTest
    fun setUp() {
        api = mockk(relaxed = true)
        dataSource = UserRemoteDataSourceImpl(api)
    }

    @Test
    fun followArtists_joinsIdsWithComma_andDelegates() = runTest {
        // Arrange
        val ids = listOf("1", "2", "3")
        val expected = NetworkResponse(data = Unit)
        coEvery { api.followArtists(ids = "1,2,3") } returns expected

        // Act
        val actual = dataSource.followArtists(ids)

        // Assert
        assertSame(expected, actual)
        coVerify(exactly = 1) { api.followArtists(ids = "1,2,3") }
    }

    @Test
    fun followUsers_joinsIdsWithComma_andDelegates() = runTest {
        // Arrange
        val ids = listOf("u1", "u2")
        coEvery { api.followUsers(ids = "u1,u2") } returns NetworkResponse(data = Unit)

        // Act
        dataSource.followUsers(ids)

        // Assert
        coVerify { api.followUsers(ids = "u1,u2") }
    }

    @Test
    fun getUserTopArtists_delegatesParams() = runTest {
        // Arrange
        val expected: NetworkResponse<PaginatedList<com.music.dzr.core.network.dto.Artist>> =
            NetworkResponse(data = mockk())
        val range = TimeRange.LongTerm
        val limit = 50
        val offset = 10
        coEvery { api.getUserTopArtists(range, limit, offset) } returns expected

        // Act
        val actual = dataSource.getUserTopArtists(range, limit, offset)

        // Assert
        assertSame(expected, actual)
        coVerify { api.getUserTopArtists(range, limit, offset) }
    }

    @Test
    fun getFollowedArtists_delegatesParams() = runTest {
        // Arrange
        val expected: NetworkResponse<FollowedArtists> = NetworkResponse(data = mockk())
        val limit = 20
        val after = "last"
        coEvery { api.getFollowedArtists(limit = limit, after = after) } returns expected

        // Act
        val actual = dataSource.getFollowedArtists(limit = limit, after = after)

        // Assert
        assertSame(expected, actual)
        coVerify { api.getFollowedArtists(limit = limit, after = after) }
    }

    @Test
    fun followPlaylist_delegates() = runTest {
        // Arrange
        val details = PlaylistFollowDetails(true)
        val playlistId = "pl"
        val publicly = true
        coEvery { api.followPlaylist(playlistId, details) } returns NetworkResponse(data = Unit)

        // Act
        dataSource.followPlaylist(playlistId, publicly)

        // Assert
        coVerify { api.followPlaylist(playlistId, details) }
    }
}


