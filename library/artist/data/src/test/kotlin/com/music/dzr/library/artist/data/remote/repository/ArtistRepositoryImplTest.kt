package com.music.dzr.library.artist.data.remote.repository

import com.music.dzr.core.error.ConnectivityError
import com.music.dzr.core.model.Market
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.network.dto.error.NetworkErrorType
import com.music.dzr.core.pagination.OffsetPageable
import com.music.dzr.core.result.isFailure
import com.music.dzr.core.result.isSuccess
import com.music.dzr.core.testing.coroutine.TestDispatcherProvider
import com.music.dzr.library.artist.data.remote.source.ArtistRemoteDataSource
import com.music.dzr.library.artist.data.remote.source.FakeArtistRemoteDataSource
import com.music.dzr.library.artist.data.repository.ArtistRepositoryImpl
import com.music.dzr.library.artist.domain.repository.ArtistRepository
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.music.dzr.core.error.NetworkError as AppNetworkError

class ArtistRepositoryImplTest {

    private lateinit var repository: ArtistRepository
    private lateinit var remoteDataSource: ArtistRemoteDataSource
    private val testScheduler = TestCoroutineScheduler()
    private val dispatchers = TestDispatcherProvider(testScheduler)

    @BeforeTest
    fun setUp() {
        remoteDataSource = FakeArtistRemoteDataSource()
        repository = ArtistRepositoryImpl(
            remoteDataSource = remoteDataSource,
            dispatchers = dispatchers
        )
    }

    @Test
    fun getArtist_returnsSuccess_whenArtistExists() = runTest(testScheduler) {
        // Arrange
        val artistId = "57dN52uHvrHOxijzpIgu3E"

        // Act
        val result = repository.getArtist(artistId)

        // Assert
        assertTrue(result.isSuccess())
    }

    @Test
    fun getMultipleArtists_returnsSuccess_andPreservesInputOrder() = runTest(testScheduler) {
        // Arrange
        val artistIds = listOf("2CIMQHirSU0MQqyYHq0eOx", "57dN52uHvrHOxijzpIgu3E")

        // Act
        val result = repository.getMultipleArtists(artistIds)

        // Assert
        assertTrue(result.isSuccess())

        val resultArtistIds = result.data.map { it.id }
        assertEquals(
            expected = artistIds,
            actual = resultArtistIds.take(2) // right order
        )
    }

    @Test
    fun getMultipleArtists_returnsEmptySuccess_whenIdsEmpty() = runTest(testScheduler) {
        // Arrange
        val artistIds = listOf<String>()

        // Act
        val result = repository.getMultipleArtists(artistIds)

        // Assert
        assertTrue(result.isSuccess())
        assertTrue(result.data.isEmpty())
    }

    @Test
    fun getArtistTopTracks_returnsSuccess_andPreservesOrder() = runTest(testScheduler) {
        // Arrange
        val artistId = "57dN52uHvrHOxijzpIgu3E"
        val trackIds = listOf(
            "4QNpBfC0zvjKqPJcyqBy9W",
            "3cHyrEgdyYRjgJKSOiOtcS",
            "2bJvI42r8EF3wxjOuDav4r",
            "4356Typ82hUiFAynbLYbPn"
        )

        // Act
        val result = repository.getArtistTopTracks(artistId)

        // Assert
        assertTrue(result.isSuccess())

        val resultTrackIds = result.data.map { it.id }
        assertEquals(
            expected = trackIds,
            actual = resultTrackIds.take(4) // right order
        )
    }

    @Test
    fun getArtistAlbums_returnsSuccess_withDefaultParams() = runTest(testScheduler) {
        // Arrange
        val artistId = "57dN52uHvrHOxijzpIgu3E"

        // Act
        val result = repository.getArtistAlbums(id = artistId)

        // Assert
        assertTrue(result.isSuccess())
        assertTrue(result.data.items.isNotEmpty())
    }

    @Test
    fun getArtistAlbums_appliesPagination_limitAndOffset() = runTest(testScheduler) {
        // Arrange
        val artistId = "57dN52uHvrHOxijzpIgu3E"
        val fake = remoteDataSource as FakeArtistRemoteDataSource
        // Ensure we have at least 2 items from defaults
        assertTrue(fake.artistAlbums.size >= 2)
        val expectedSecondId = fake.artistAlbums[1].id

        // Act
        val result = repository.getArtistAlbums(
            id = artistId,
            pageable = OffsetPageable(limit = 1, offset = 1)
        )

        // Assert
        assertTrue(result.isSuccess())
        assertEquals(1, result.data.items.size)
        assertEquals(expectedSecondId, result.data.items.first().id)
    }

    @Test
    fun getArtistAlbums_excludesAppearsOn_whenFlagIsFalse() = runTest(testScheduler) {
        // Arrange
        val artistId = "57dN52uHvrHOxijzpIgu3E"

        // Act
        val result = repository.getArtistAlbums(
            id = artistId,
            includeAppearsOn = false
        )

        // Assert
        assertTrue(result.isSuccess())
        assertTrue(result.data.items.none { it.justAppearsOn })
    }

    @Test
    fun getArtistAlbums_filtersByMarket_whenSpecified() = runTest(testScheduler) {
        // Arrange
        val artistId = "57dN52uHvrHOxijzpIgu3E"
        val fake = remoteDataSource as FakeArtistRemoteDataSource
        // Compute expected count using the same predicate as FakeArtistRemoteDataSource
        val market = Market("US")
        val expectedCount = fake.artistAlbums
            .filter { it.availableMarkets == null || market.code in it.availableMarkets }
            .size

        // Act
        val result = repository.getArtistAlbums(
            id = artistId,
            market = market
        )

        // Assert
        assertTrue(result.isSuccess())
        assertEquals(expectedCount, result.data.items.size)
    }

    @Test
    fun getArtist_returnsConnectivityTimeout_onTimeoutError() = runTest(testScheduler) {
        // Arrange
        val fake = remoteDataSource as FakeArtistRemoteDataSource
        fake.forcedError = NetworkError(
            type = NetworkErrorType.Timeout,
            message = "timeout",
            code = 0
        )

        // Act
        val result = repository.getArtist("any")

        // Assert
        assertTrue(result.isFailure())
        val error = result.error
        assertTrue(error is ConnectivityError.Timeout)
    }

    @Test
    fun getMultipleArtists_returnsUnauthorized_onHttp401() = runTest(testScheduler) {
        // Arrange
        val fake = remoteDataSource as FakeArtistRemoteDataSource
        fake.forcedError = NetworkError(
            type = NetworkErrorType.HttpException,
            message = "unauthorized",
            code = 401
        )

        // Act
        val result = repository.getMultipleArtists(listOf("doesn't", "matter"))

        // Assert
        assertTrue(result.isFailure())
        val error = result.error
        assertTrue(error is AppNetworkError.Unauthorized)
    }
}