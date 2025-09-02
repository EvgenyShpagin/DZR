package com.music.dzr.library.artist.data.remote.source

import com.music.dzr.core.network.dto.AlbumGroup
import com.music.dzr.core.network.dto.Artist
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Tracks
import com.music.dzr.library.artist.data.remote.api.ArtistApi
import com.music.dzr.library.artist.data.remote.dto.ArtistAlbum
import com.music.dzr.library.artist.data.remote.dto.Artists
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame

internal class ArtistRemoteDataSourceTest {

    private lateinit var api: ArtistApi
    private lateinit var dataSource: ArtistRemoteDataSource

    @BeforeTest
    fun setUp() {
        api = mockk(relaxed = true)
        dataSource = ArtistRemoteDataSourceImpl(api)
    }

    @Test
    fun getArtist_delegatesCall() = runTest {
        val id = "artistId"
        val expected = NetworkResponse(data = mockk<Artist>())
        coEvery { api.getArtist(id) } returns expected

        val actual = dataSource.getArtist(id)

        assertSame(expected, actual)
        coVerify { api.getArtist(id) }
    }

    @Test
    fun getMultipleArtists_joinsIdsWithComma_andDelegates() = runTest {
        val ids = listOf("1", "2", "3")
        val expected = NetworkResponse(data = mockk<Artists>())
        coEvery { api.getMultipleArtists(ids = "1,2,3") } returns expected

        val actual = dataSource.getMultipleArtists(ids)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.getMultipleArtists(ids = "1,2,3") }
    }

    @Test
    fun getArtistAlbums_joinsIncludeGroups_andDelegates() = runTest {
        val id = "artistId"
        val includeGroups = listOf(AlbumGroup.Album, AlbumGroup.Single)
        val market = "US"
        val limit = 20
        val offset = 0
        val expected = NetworkResponse(data = mockk<PaginatedList<ArtistAlbum>>())
        coEvery { api.getArtistAlbums(id, "album,single", market, limit, offset) } returns expected

        val actual = dataSource.getArtistAlbums(id, includeGroups, market, limit, offset)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.getArtistAlbums(id, "album,single", market, limit, offset) }
    }

    @Test
    fun getArtistTopTracks_delegatesParams() = runTest {
        val id = "artistId"
        val market = "ES"
        val expected = NetworkResponse(data = mockk<Tracks>())
        coEvery { api.getArtistTopTracks(id, market) } returns expected

        val actual = dataSource.getArtistTopTracks(id, market)

        assertSame(expected, actual)
        coVerify { api.getArtistTopTracks(id, market) }
    }
}