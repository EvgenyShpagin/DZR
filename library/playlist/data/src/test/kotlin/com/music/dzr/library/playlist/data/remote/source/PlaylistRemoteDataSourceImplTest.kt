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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame

class PlaylistRemoteDataSourceImplTest {

    private lateinit var api: PlaylistApi
    private lateinit var dataSource: PlaylistRemoteDataSourceImpl

    @BeforeTest
    fun setUp() {
        api = mockk(relaxed = true)
        dataSource = PlaylistRemoteDataSourceImpl(api)
    }

    @Test
    fun getPlaylist_delegatesAllParams() = runTest {
        // Arrange
        val playlistId = "pl"
        val market = "US"
        val fields = PlaylistFields(listOf(PlaylistField.Id, PlaylistField.Name))
        val expected = NetworkResponse<PlaylistWithPaginatedTracks>(data = mockk())
        coEvery { api.getPlaylist(playlistId, market, fields) } returns expected

        // Act
        val actual = dataSource.getPlaylist(playlistId, market, fields)

        // Assert
        assertSame(expected, actual)
        coVerify { api.getPlaylist(playlistId, market, fields) }
    }

    @Test
    fun updatePlaylistTracks_delegates() = runTest {
        // Arrange
        val playlistId = "pl"
        val update = mockk<PlaylistItemsUpdate>()
        val expected = NetworkResponse(data = SnapshotId("snap"))
        coEvery { api.updatePlaylistTracks(playlistId, update) } returns expected

        // Act
        val actual = dataSource.updatePlaylistTracks(playlistId, update)

        // Assert
        assertSame(expected, actual)
        coVerify { api.updatePlaylistTracks(playlistId, update) }
    }

    @Test
    fun addTracksToPlaylist_delegates() = runTest {
        // Arrange
        val playlistId = "pl"
        val additions = mockk<TrackAdditions>()
        val expected = NetworkResponse(data = SnapshotId("snap2"))
        coEvery { api.addTracksToPlaylist(playlistId, additions) } returns expected

        // Act
        val actual = dataSource.addTracksToPlaylist(playlistId, additions)

        // Assert
        assertSame(expected, actual)
        coVerify { api.addTracksToPlaylist(playlistId, additions) }
    }

    @Test
    fun getCurrentUserPlaylists_delegates() = runTest {
        // Arrange
        val limit = 20
        val offset = 40
        val expected: NetworkResponse<PaginatedList<PlaylistWithTracksInfo>> =
            NetworkResponse(data = mockk())
        coEvery { api.getCurrentUserPlaylists(limit = limit, offset = offset) } returns expected

        // Act
        val actual = dataSource.getCurrentUserPlaylists(limit = limit, offset = offset)

        // Assert
        assertSame(expected, actual)
        coVerify { api.getCurrentUserPlaylists(limit = limit, offset = offset) }
    }

    @Test
    fun uploadCustomPlaylistCover_delegates() = runTest {
        // Arrange
        val playlistId = "pl"
        val expectedImageRequestBody = ByteArray(0).toRequestBody("image/jpeg".toMediaType())
        val expected = NetworkResponse(data = Unit)
        coEvery { api.uploadCustomPlaylistCover(playlistId, expectedImageRequestBody) } returns expected

        // Act
        dataSource.uploadCustomPlaylistCover(playlistId, ByteArray(0))

        // Assert
        coVerify {
            api.uploadCustomPlaylistCover(
                playlistId = playlistId,
                encodedImageData = any()
            )
        }
    }

    @Test
    fun removePlaylistTracks_delegates() = runTest {
        // Arrange
        val playlistId = "pl"
        val body = mockk<TrackRemovals>()
        val expected = NetworkResponse(data = SnapshotId("snap3"))
        coEvery { api.removePlaylistTracks(playlistId, body) } returns expected

        // Act
        val actual = dataSource.removePlaylistTracks(playlistId, body)

        // Assert
        assertSame(expected, actual)
        coVerify { api.removePlaylistTracks(playlistId, body) }
    }

    @Test
    fun createPlaylist_delegates() = runTest {
        // Arrange
        val userId = "user"
        val details = mockk<NewPlaylistDetails>()
        val expected: NetworkResponse<PlaylistWithPaginatedTracks> = NetworkResponse(data = mockk())
        coEvery { api.createPlaylist(userId, details) } returns expected

        // Act
        val actual = dataSource.createPlaylist(userId, details)

        // Assert
        assertSame(expected, actual)
        coVerify { api.createPlaylist(userId, details) }
    }

    @Test
    fun getPlaylistTracks_delegatesAllParams() = runTest {
        // Arrange
        val playlistId = "pl"
        val market = "DE"
        val limit = 50
        val offset = 100
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
        val expected = NetworkResponse<PaginatedList<PlaylistTrack>>(data = mockk())
        coEvery { api.getPlaylistTracks(playlistId, market, fields, limit, offset) } returns expected

        // Act
        val actual = dataSource.getPlaylistTracks(
            playlistId,
            market,
            PlaylistFields.items(
                listOf(
                    PlaylistFields.group(
                        PlaylistField.Track,
                        listOf(PlaylistFields(listOf(PlaylistField.Id)))
                    )
                )
            ),
            limit,
            offset
        )

        // Assert
        assertSame(expected, actual)
        coVerify { api.getPlaylistTracks(playlistId, market, fields, limit, offset) }
    }

    @Test
    fun getUserPlaylists_delegates() = runTest {
        // Arrange
        val userId = "user"
        val limit = 10
        val offset = 30
        val expected: NetworkResponse<PaginatedList<PlaylistWithTracks>> =
            NetworkResponse(data = mockk())
        coEvery { api.getUserPlaylists(userId, limit, offset) } returns expected

        // Act
        val actual = dataSource.getUserPlaylists(userId, limit, offset)

        // Assert
        assertSame(expected, actual)
        coVerify { api.getUserPlaylists(userId, limit, offset) }
    }

    @Test
    fun changePlaylistDetails_delegates() = runTest {
        // Arrange
        val playlistId = "pl"
        val body = mockk<PlaylistDetailsUpdate>()
        coEvery { api.changePlaylistDetails(playlistId, body) } returns NetworkResponse(data = Unit)

        // Act
        dataSource.changePlaylistDetails(playlistId, body)

        // Assert
        coVerify { api.changePlaylistDetails(playlistId, body) }
    }
}
