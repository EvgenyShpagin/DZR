package com.music.dzr.library.album.data.repository

import com.music.dzr.core.model.Market
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.network.dto.error.NetworkErrorType
import com.music.dzr.core.pagination.OffsetPageable
import com.music.dzr.core.result.isSuccess
import com.music.dzr.core.testing.assertion.assertFailureEquals
import com.music.dzr.core.testing.assertion.assertSuccess
import com.music.dzr.core.testing.coroutine.TestDispatcherProvider
import com.music.dzr.library.album.data.remote.source.TestAlbumRemoteDataSource
import com.music.dzr.library.album.domain.repository.AlbumRepository
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.music.dzr.core.error.NetworkError as AppNetworkError

class AlbumRepositoryImplTest {

    private lateinit var repository: AlbumRepository
    private lateinit var remoteDataSource: TestAlbumRemoteDataSource
    private val testScheduler = TestCoroutineScheduler()
    private val dispatchers = TestDispatcherProvider(testScheduler)

    @BeforeTest
    fun setUp() {
        remoteDataSource = TestAlbumRemoteDataSource()
        repository = AlbumRepositoryImpl(
            remoteDataSource = remoteDataSource,
            dispatchers = dispatchers
        )
        // Ensure saved list is deterministic for tests that assert exact contents
        remoteDataSource.userSavedAlbums.clear()
    }

    @Test
    fun getAlbum_returnsSuccess_andMapsFields() = runTest(testScheduler) {
        // Arrange
        val album = remoteDataSource.albums.first()

        // Act
        val result = repository.getAlbum(album.id)

        // Assert
        assertSuccess(result)
        val a = result.data
        assertEquals(album.id, a.id)
        assertEquals(album.name, a.name)
        assertEquals(album.tracks.items.size, a.tracks.size)
    }

    @Test
    fun getMultipleAlbums_returnsSuccess_andPreservesInputOrder() = runTest(testScheduler) {
        // Arrange
        val a1 = remoteDataSource.albums[0]
        val a2 = remoteDataSource.albums[1]
        val ids = listOf(a1.id, a2.id)

        // Act
        val result = repository.getMultipleAlbums(ids)

        // Assert
        assertSuccess(result)
        val list = result.data
        assertEquals(ids, list.take(2).map { it.id })
    }

    @Test
    fun getAlbumTracks_respectsPagination() = runTest(testScheduler) {
        // Arrange
        val album = remoteDataSource.albums.first()
        val pageable = OffsetPageable(limit = 2, offset = 1)

        // Act
        val result = repository.getAlbumTracks(id = album.id, pageable = pageable)

        // Assert
        assertSuccess(result)
        val page = result.data
        assertEquals(2, page.items.size)
        val all = remoteDataSource.albumTracks
        val expected = all.drop(pageable.offset).take(pageable.limit).map { it.trackNumber }
        assertEquals(expected, page.items.map { it.trackNumber })
    }

    @Test
    fun saveAndRemoveAlbumsForUser_updatesSavedList() = runTest(testScheduler) {
        // Arrange
        val a1 = remoteDataSource.albums[0]
        val a2 = remoteDataSource.albums[1]

        // Act
        val saveRes = repository.saveAlbumsForUser(listOf(a1.id, a2.id))
        assertTrue(saveRes.isSuccess())

        val savedRes = repository.getUserSavedAlbums()
        assertSuccess(savedRes)
        assertTrue(savedRes.data.items.map { it.id }.containsAll(listOf(a1.id, a2.id)))

        val removeRes = repository.removeAlbumsForUser(listOf(a1.id))
        assertSuccess(removeRes)

        val savedRes2 = repository.getUserSavedAlbums()
        assertSuccess(savedRes2)
        assertEquals(listOf(a2.id), savedRes2.data.items.map { it.id })
    }

    @Test
    fun getUserSavedAlbums_respectsPagination() = runTest(testScheduler) {
        // Arrange
        val ids = remoteDataSource.albums.take(3).map { it.id }
        repository.saveAlbumsForUser(ids)

        // Act
        val pageable = OffsetPageable(limit = 2, offset = 1)
        val result = repository.getUserSavedAlbums(pageable = pageable)

        // Assert
        assertSuccess(result)
        assertEquals(2, result.data.items.size)
    }

    @Test
    fun anyCall_returnsUnauthorized_onHttp401() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.forcedError = NetworkError(
            type = NetworkErrorType.HttpException,
            message = "unauthorized",
            code = 401
        )

        // Act
        val result = repository.getAlbum("does_not_matter", market = Market.Unspecified)

        // Assert
        assertFailureEquals(AppNetworkError.Unauthorized, result)
    }
}
