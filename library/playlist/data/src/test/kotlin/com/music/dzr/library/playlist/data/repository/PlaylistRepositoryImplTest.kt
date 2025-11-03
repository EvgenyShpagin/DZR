package com.music.dzr.library.playlist.data.repository

import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.network.dto.ExternalUrls
import com.music.dzr.core.pagination.OffsetPageable
import com.music.dzr.core.pagination.Page
import com.music.dzr.core.testing.assertion.assertFailureEquals
import com.music.dzr.core.testing.assertion.assertSuccess
import com.music.dzr.core.testing.coroutine.TestDispatcherProvider
import com.music.dzr.core.testing.data.networkDetailedTracksTestData
import com.music.dzr.library.playlist.data.mapper.toNetwork
import com.music.dzr.library.playlist.data.remote.source.TestPlaylistRemoteDataSource
import com.music.dzr.library.playlist.domain.model.InsertPosition
import com.music.dzr.library.playlist.domain.model.PlaylistDetails
import com.music.dzr.library.playlist.domain.model.PlaylistVersion
import com.music.dzr.library.playlist.domain.repository.PlaylistRepository
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant
import com.music.dzr.core.error.NetworkError as DomainNetworkError
import com.music.dzr.core.network.dto.Image as NetImage
import com.music.dzr.core.network.dto.Playlist as NetPlaylist
import com.music.dzr.core.network.dto.PlaylistTrack as NetPlaylistTrack
import com.music.dzr.core.network.dto.error.NetworkError as NetworkErrorDto
import com.music.dzr.core.network.dto.error.NetworkErrorType as NetworkErrorTypeDto

class PlaylistRepositoryImplTest {

    private lateinit var remoteDataSource: TestPlaylistRemoteDataSource
    private lateinit var repository: PlaylistRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatchers = TestDispatcherProvider(testScheduler)
    private val externalScope = ApplicationScope(testDispatchers)

    @BeforeTest
    fun setUp() {
        remoteDataSource = TestPlaylistRemoteDataSource()
        repository = PlaylistRepositoryImpl(
            remoteDataSource = remoteDataSource,
            dispatchers = testDispatchers,
            externalScope = externalScope
        )
    }

    private fun buildPlaylistTrack(
        uri: String,
        addedAt: Instant = Instant.fromEpochMilliseconds(1_700_000_000_000)
    ) = NetPlaylistTrack(
        addedAt = addedAt,
        addedBy = remoteDataSource.owner,
        isLocal = false,
        track = networkDetailedTracksTestData.find { it.uri == uri }!!
    )

    private fun seedPlaylist(
        id: String,
        name: String,
        trackUris: List<String>
    ) {
        // Seed tracks first
        trackUris.forEach { uri -> remoteDataSource.seedTracks(buildPlaylistTrack(uri)) }

        val playlist = NetPlaylist(
            collaborative = false,
            description = "desc $id",
            externalUrls = ExternalUrls(spotify = "https://open.spotify.com/playlist/$id"),
            followers = null,
            href = "https://api.spotify.com/v1/playlists/$id",
            id = id,
            images = listOf(
                NetImage(url = "https://img/$id.png", height = 640, width = 640)
            ),
            name = name,
            owner = remoteDataSource.owner,
            public = true,
            snapshotId = remoteDataSource.snapshot.snapshotId,
            tracks = trackUris.map { uri -> buildPlaylistTrack(uri) },
            type = "playlist",
            uri = "spotify:playlist:$id"
        )
        remoteDataSource.seedPlaylists(playlist)
    }

    @Test
    fun getPlaylist_returnsSuccess_andMapsFields() = runTest(testScheduler) {
        // Arrange
        val pid = "pl_1"
        seedPlaylist(
            pid,
            name = "My Playlist",
            trackUris = listOf("spotify:track:track_1", "spotify:track:track_2")
        )

        // Act
        val result = repository.getPlaylist(pid)

        // Assert
        assertSuccess(result)
        val p = result.data
        assertEquals(pid, p.id)
        assertEquals("My Playlist", p.name)
        assertEquals(2, p.tracksCount)
    }

    @Test
    fun changePlaylistDetails_updatesData_andReturnsSuccess() = runTest(testScheduler) {
        // Arrange
        val pid = "pl_change"
        seedPlaylist(pid, name = "Old Name", trackUris = emptyList())

        // Act
        val result = repository.changePlaylistDetails(
            playlistId = pid,
            details = PlaylistDetails(
                name = "New Name",
                description = "d",
                isPublic = true,
                isCollaborative = false
            )
        )

        // Assert
        assertSuccess(result)
        val getRes = repository.getPlaylist(pid)
        assertSuccess(getRes)
        assertEquals("New Name", getRes.data.name)
    }

    @Test
    fun getPlaylistTracks_respectsPagination() = runTest(testScheduler) {
        // Arrange
        val pid = "pl_paged"
        seedPlaylist(
            pid, name = "Paged", trackUris = listOf(
                "spotify:track:track_1",
                "spotify:track:track_2",
                "spotify:track:track_3"
            )
        )
        val pageable = OffsetPageable(limit = 2, offset = 1)

        // Act
        val pageRes = repository.getPlaylistTracks(pid, pageable = pageable)

        // Assert
        assertSuccess(pageRes)
        val page: Page<*> = pageRes.data
        assertEquals(2, page.items.size)
    }

    @Test
    fun replaceAll_replacesTracks_andReturnsNewVersion() = runTest(testScheduler) {
        // Arrange
        val pid = "pl_replace"
        remoteDataSource.seedTracks(
            buildPlaylistTrack("spotify:track:track_1"),
            buildPlaylistTrack("spotify:track:track_2")
        )
        seedPlaylist(pid, name = "Replace", trackUris = listOf("spotify:track:track_1"))

        // Act
        val result = repository.replaceAll(
            playlistId = pid,
            newItemIds = listOf("track_2"),
        )

        // Assert
        assertSuccess(result)
        val version: PlaylistVersion = result.data
        assertEquals(remoteDataSource.snapshot.snapshotId, version.toNetwork())

        val pageRes = repository.getPlaylistTracks(pid)
        assertSuccess(pageRes)
        assertEquals(1, pageRes.data.items.size)
    }

    @Test
    fun moveRange_reordersItems() = runTest(testScheduler) {
        // Arrange
        val pid = "pl_move"
        seedPlaylist(
            pid, name = "Move", trackUris = listOf(
                "spotify:track:track_1",
                "spotify:track:track_2",
                "spotify:track:track_3"
            )
        )

        // Act: move first item (index 0, length 1) to position before index 2
        val result = repository.moveRange(
            playlistId = pid,
            fromIndex = 0,
            length = 1,
            toIndex = 2,
        )

        // Assert
        assertSuccess(result)
        val tracks = repository.getPlaylistTracks(pid)
        assertSuccess(tracks)
        val uris = tracks.data.items.map { it.track.id }
        // After moving first to before index 2: order should be 2,1,3 by track ids
        assertEquals(listOf("track_2", "track_1", "track_3"), uris)
    }

    @Test
    fun addTracksToPlaylist_insertsAtPosition_andDeduplicates() = runTest(testScheduler) {
        // Arrange
        val pid = "pl_add"
        // Seed track_2 as candidate to add; also duplicate of track_1
        remoteDataSource.seedTracks(
            buildPlaylistTrack("spotify:track:track_2"),
            buildPlaylistTrack("spotify:track:track_1")
        )
        seedPlaylist(pid, name = "Add", trackUris = listOf("spotify:track:track_1"))

        // Act
        val result = repository.addTracksToPlaylist(
            playlistId = pid,
            trackIds = listOf("track_2", "track_1"),
            position = InsertPosition.At(1)
        )

        // Assert
        assertSuccess(result)
        val tracks = repository.getPlaylistTracks(pid)
        assertSuccess(tracks)
        val uris = tracks.data.items.map { it.track.id }
        // Should be 1,2 (dedup keeps unique by uri, but we assert domain ids)
        assertEquals(listOf("track_1", "track_2"), uris)
    }

    @Test
    fun removePlaylistTracks_removesSpecified() = runTest(testScheduler) {
        // Arrange
        val pid = "pl_remove"
        remoteDataSource.seedTracks(
            buildPlaylistTrack("spotify:track:track_1"),
            buildPlaylistTrack("spotify:track:track_2")
        )
        seedPlaylist(
            id = pid,
            name = "Remove",
            trackUris = listOf(
                "spotify:track:track_1",
                "spotify:track:track_2"
            )
        )

        // Act
        val result = repository.removePlaylistTracks(
            playlistId = pid,
            trackIds = listOf("track_1"),
        )

        // Assert
        assertSuccess(result)
        val tracks = repository.getPlaylistTracks(pid)
        assertSuccess(tracks)
        val uris = tracks.data.items.map { it.track.id }
        assertEquals(listOf("track_2"), uris)
    }

    @Test
    fun getCurrentUserPlaylists_returnsMappedList() = runTest(testScheduler) {
        // Arrange
        seedPlaylist("pl_u1", name = "U1", trackUris = listOf("spotify:track:track_1"))
        seedPlaylist("pl_u2", name = "U2", trackUris = emptyList())
        val pageable = OffsetPageable(limit = 2, offset = 0)

        // Act
        val result = repository.getCurrentUserPlaylists(pageable)

        // Assert
        assertSuccess(result)
        val page = result.data
        assertEquals(2, page.items.size)
    }

    @Test
    fun getUserPlaylists_filtersByOwner() = runTest(testScheduler) {
        // Arrange: create two playlists owned by fake owner; method filters by provided userId
        seedPlaylist("pl_owner1", name = "O1", trackUris = emptyList())
        seedPlaylist("pl_owner2", name = "O2", trackUris = emptyList())

        // Act
        val result = repository.getUserPlaylists(
            userId = remoteDataSource.owner.id,
        )

        // Assert
        assertSuccess(result)
        assertTrue(result.data.items.size >= 2)
    }

    @Test
    fun createPlaylist_returnsCreated_andGetCoverThenUploadCustomCover() = runTest(testScheduler) {
        // Act
        val createRes = repository.createPlaylist(
            userId = remoteDataSource.owner.id,
            details = PlaylistDetails(
                name = "Created",
                description = "d",
                isPublic = true,
                isCollaborative = false
            )
        )

        assertSuccess(createRes)
        val created = createRes.data

        val coverRes = repository.getPlaylistCoverImage(created.id)
        assertSuccess(coverRes)
        // initially empty
        assertEquals(0, coverRes.data.size)

        val uploadRes = repository.uploadCustomPlaylistCover(
            playlistId = created.id,
            jpegImage = byteArrayOf(1, 2, 3)
        )
        assertSuccess(uploadRes)
        val coverRes2 = repository.getPlaylistCoverImage(created.id)
        assertSuccess(coverRes2)
        assertEquals(1, coverRes2.data.size)
    }

    @Test
    fun anyCall_returnsFailure_whenRemoteReturns401() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.forcedError = NetworkErrorDto(
            type = NetworkErrorTypeDto.HttpException,
            message = "Unauthorized",
            code = 401,
            reason = null
        )

        // Act
        val result = repository.getCurrentUserPlaylists()

        // Assert
        assertFailureEquals(DomainNetworkError.Unauthorized, result)
    }
}