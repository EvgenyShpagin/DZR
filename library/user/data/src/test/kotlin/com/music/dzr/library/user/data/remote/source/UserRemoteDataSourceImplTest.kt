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
        val ids = listOf("1", "2", "3")
        val expected = NetworkResponse(data = Unit)
        coEvery { api.followArtists(ids = "1,2,3") } returns expected

        val actual = dataSource.followArtists(ids)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.followArtists(ids = "1,2,3") }
    }

    @Test
    fun followUsers_joinsIdsWithComma_andDelegates() = runTest {
        val ids = listOf("u1", "u2")
        coEvery { api.followUsers(ids = "u1,u2") } returns NetworkResponse(data = Unit)

        dataSource.followUsers(ids)

        coVerify { api.followUsers(ids = "u1,u2") }
    }

    @Test
    fun getUsersTopArtists_delegatesParams() = runTest {
        val expected: NetworkResponse<PaginatedList<com.music.dzr.core.network.dto.Artist>> =
            NetworkResponse(data = mockk())
        coEvery { api.getUsersTopArtists(TimeRange.LongTerm, 50, 10) } returns expected

        val actual = dataSource.getUsersTopArtists(TimeRange.LongTerm, 50, 10)

        assertSame(expected, actual)
        coVerify { api.getUsersTopArtists(TimeRange.LongTerm, 50, 10) }
    }

    @Test
    fun getFollowedArtists_delegatesParams() = runTest {
        val expected: NetworkResponse<FollowedArtists> = NetworkResponse(data = mockk())
        coEvery { api.getFollowedArtists(limit = 20, after = "last") } returns expected

        val actual = dataSource.getFollowedArtists(limit = 20, after = "last")

        assertSame(expected, actual)
        coVerify { api.getFollowedArtists(limit = 20, after = "last") }
    }

    @Test
    fun followPlaylist_delegates() = runTest {
        val details = PlaylistFollowDetails(true)
        coEvery { api.followPlaylist("pl", details) } returns NetworkResponse(data = Unit)

        dataSource.followPlaylist("pl", true)

        coVerify { api.followPlaylist("pl", details) }
    }
}


