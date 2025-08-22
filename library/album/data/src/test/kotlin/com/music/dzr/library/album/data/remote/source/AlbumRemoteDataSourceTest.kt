package com.music.dzr.library.album.data.remote.source

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.library.album.data.remote.api.AlbumApi
import com.music.dzr.library.album.data.remote.dto.Album
import com.music.dzr.library.album.data.remote.dto.AlbumTrack
import com.music.dzr.library.album.data.remote.dto.Albums
import com.music.dzr.library.album.data.remote.dto.SavedAlbum
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame

internal class AlbumRemoteDataSourceTest {

    private lateinit var api: AlbumApi
    private lateinit var dataSource: AlbumRemoteDataSource

    @BeforeTest
    fun setUp() {
        api = mockk(relaxed = true)
        dataSource = AlbumRemoteDataSource(api)
    }

    @Test
    fun getAlbum_delegatesCall() = runTest {
        val id = "albumId"
        val market = "US"
        val expected = NetworkResponse(data = mockk<Album>())
        coEvery { api.getAlbum(id, market) } returns expected

        val actual = dataSource.getAlbum(id, market)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.getAlbum(id, market) }
    }

    @Test
    fun getMultipleAlbums_joinsIdsWithComma_andDelegates() = runTest {
        val ids = listOf("1", "2", "3")
        val market = "US"
        val expected = NetworkResponse(data = mockk<Albums>())
        coEvery { api.getMultipleAlbums(ids = "1,2,3", market = market) } returns expected

        val actual = dataSource.getMultipleAlbums(ids, market)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.getMultipleAlbums(ids = "1,2,3", market = market) }
    }

    @Test
    fun getAlbumTracks_delegatesCall() = runTest {
        val id = "albumId"
        val market = "US"
        val limit = 10
        val offset = 5
        val expected = NetworkResponse(data = mockk<PaginatedList<AlbumTrack>>())
        coEvery { api.getAlbumTracks(id, market, limit, offset) } returns expected

        val actual = dataSource.getAlbumTracks(id, market, limit, offset)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.getAlbumTracks(id, market, limit, offset) }
    }

    @Test
    fun getUserSavedAlbums_delegatesCall() = runTest {
        val limit = 20
        val offset = 10
        val market = "FR"
        val expected = NetworkResponse(data = mockk<PaginatedList<SavedAlbum>>())
        coEvery { api.getUserSavedAlbums(limit, offset, market) } returns expected

        val actual = dataSource.getUserSavedAlbums(limit, offset, market)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.getUserSavedAlbums(limit, offset, market) }
    }

    @Test
    fun saveAlbumsForUser_joinsIdsWithComma_andDelegates() = runTest {
        val ids = listOf("a", "b", "c")
        val expected = NetworkResponse(data = Unit)
        coEvery { api.saveAlbumsForUser(ids = "a,b,c") } returns expected

        val actual = dataSource.saveAlbumsForUser(ids)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.saveAlbumsForUser(ids = "a,b,c") }
    }

    @Test
    fun removeAlbumsForUser_joinsIdsWithComma_andDelegates() = runTest {
        val ids = listOf("x", "y")
        val expected = NetworkResponse(data = Unit)
        coEvery { api.removeAlbumsForUser(ids = "x,y") } returns expected

        val actual = dataSource.removeAlbumsForUser(ids)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.removeAlbumsForUser(ids = "x,y") }
    }
}