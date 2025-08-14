package com.music.dzr.library.playlist.data.remote.source

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.PlaylistTrack
import com.music.dzr.core.network.dto.PlaylistWithPaginatedTracks
import com.music.dzr.core.network.dto.PlaylistWithTracks
import com.music.dzr.core.network.dto.PlaylistWithTracksInfo
import com.music.dzr.core.network.dto.SnapshotId
import com.music.dzr.library.playlist.data.remote.api.PlaylistApi
import com.music.dzr.library.playlist.data.remote.dto.NewPlaylistDetails
import com.music.dzr.library.playlist.data.remote.dto.PlaylistDetailsUpdate
import com.music.dzr.library.playlist.data.remote.dto.PlaylistField
import com.music.dzr.library.playlist.data.remote.dto.PlaylistFields
import com.music.dzr.library.playlist.data.remote.dto.PlaylistItemsUpdate
import com.music.dzr.library.playlist.data.remote.dto.TrackAdditions
import com.music.dzr.library.playlist.data.remote.dto.TrackRemovals
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.RequestBody
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame

class PlaylistRemoteDataSourceTest {

    private lateinit var api: PlaylistApi
    private lateinit var dataSource: PlaylistRemoteDataSource

    @BeforeTest
    fun setUp() {
        api = mockk(relaxed = true)
        dataSource = PlaylistRemoteDataSource(api)
    }

    @Test
    fun getPlaylist_delegatesAllParams() = runTest {
        val expected = NetworkResponse<PlaylistWithPaginatedTracks>(data = mockk())
        val fields = PlaylistFields(listOf(PlaylistField.Id, PlaylistField.Name))
        coEvery { api.getPlaylist("pl", "US", fields) } returns expected

        val actual = dataSource.getPlaylist("pl", "US", fields)

        assertSame(expected, actual)
        coVerify { api.getPlaylist("pl", "US", fields) }
    }

    @Test
    fun updatePlaylistTracks_delegates() = runTest {
        val update = mockk<PlaylistItemsUpdate>()
        val expected = NetworkResponse(data = SnapshotId("snap"))
        coEvery { api.updatePlaylistTracks("pl", update) } returns expected

        val actual = dataSource.updatePlaylistTracks("pl", update)

        assertSame(expected, actual)
        coVerify { api.updatePlaylistTracks("pl", update) }
    }

    @Test
    fun addTracksToPlaylist_delegates() = runTest {
        val additions = mockk<TrackAdditions>()
        val expected = NetworkResponse(data = SnapshotId("snap2"))
        coEvery { api.addTracksToPlaylist("pl", additions) } returns expected

        val actual = dataSource.addTracksToPlaylist("pl", additions)

        assertSame(expected, actual)
        coVerify { api.addTracksToPlaylist("pl", additions) }
    }

    @Test
    fun getCurrentUserPlaylists_delegates() = runTest {
        val expected: NetworkResponse<PaginatedList<PlaylistWithTracksInfo>> =
            NetworkResponse(data = mockk())
        coEvery { api.getCurrentUserPlaylists(limit = 20, offset = 40) } returns expected

        val actual = dataSource.getCurrentUserPlaylists(limit = 20, offset = 40)

        assertSame(expected, actual)
        coVerify { api.getCurrentUserPlaylists(limit = 20, offset = 40) }
    }

    @Test
    fun uploadCustomPlaylistCover_delegates() = runTest {
        val body = mockk<RequestBody>()
        val expected = NetworkResponse(data = Unit)
        coEvery { api.uploadCustomPlaylistCover("pl", body) } returns expected

        val actual = dataSource.uploadCustomPlaylistCover("pl", body)

        assertSame(expected, actual)
        coVerify { api.uploadCustomPlaylistCover("pl", body) }
    }

    @Test
    fun removePlaylistTracks_delegates() = runTest {
        val body = mockk<TrackRemovals>()
        val expected = NetworkResponse(data = SnapshotId("snap3"))
        coEvery { api.removePlaylistTracks("pl", body) } returns expected

        val actual = dataSource.removePlaylistTracks("pl", body)

        assertSame(expected, actual)
        coVerify { api.removePlaylistTracks("pl", body) }
    }

    @Test
    fun createPlaylist_delegates() = runTest {
        val details = mockk<NewPlaylistDetails>()
        val expected: NetworkResponse<PlaylistWithPaginatedTracks> = NetworkResponse(data = mockk())
        coEvery { api.createPlaylist("user", details) } returns expected

        val actual = dataSource.createPlaylist("user", details)

        assertSame(expected, actual)
        coVerify { api.createPlaylist("user", details) }
    }

    @Test
    fun getPlaylistTracks_delegatesAllParams() = runTest {
        val expected = NetworkResponse<PaginatedList<PlaylistTrack>>(data = mockk())
        val fields = PlaylistFields.items(
            listOf(
                PlaylistFields.group(
                    PlaylistField.Track,
                    listOf(
                        PlaylistFields(listOf(PlaylistField.Id))
                    )
                )
            )
        )
        coEvery { api.getPlaylistTracks("pl", "DE", fields, 50, 100) } returns expected

        val actual = dataSource.getPlaylistTracks(
            "pl",
            "DE",
            PlaylistFields.items(
                listOf(
                    PlaylistFields.group(
                        PlaylistField.Track,
                        listOf(PlaylistFields(listOf(PlaylistField.Id)))
                    )
                )
            ),
            50,
            100
        )

        assertSame(expected, actual)
        coVerify { api.getPlaylistTracks("pl", "DE", fields, 50, 100) }
    }

    @Test
    fun getUserPlaylists_delegates() = runTest {
        val expected: NetworkResponse<PaginatedList<PlaylistWithTracks>> =
            NetworkResponse(data = mockk())
        coEvery { api.getUserPlaylists("user", 10, 30) } returns expected

        val actual = dataSource.getUserPlaylists("user", 10, 30)

        assertSame(expected, actual)
        coVerify { api.getUserPlaylists("user", 10, 30) }
    }

    @Test
    fun changePlaylistDetails_delegates() = runTest {
        val body = mockk<PlaylistDetailsUpdate>()
        coEvery { api.changePlaylistDetails("pl", body) } returns NetworkResponse(data = Unit)

        dataSource.changePlaylistDetails("pl", body)

        coVerify { api.changePlaylistDetails("pl", body) }
    }
}
