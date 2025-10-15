package com.music.dzr.library.track.data.repository

import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.model.Market
import com.music.dzr.core.network.dto.ExternalUrls
import com.music.dzr.core.network.dto.SimplifiedArtist
import com.music.dzr.core.pagination.OffsetPageable
import com.music.dzr.core.pagination.Page
import com.music.dzr.core.result.Result
import com.music.dzr.core.result.isSuccess
import com.music.dzr.core.testing.coroutine.TestDispatcherProvider
import com.music.dzr.core.testing.data.networkDetailedTracksTestData
import com.music.dzr.library.track.data.remote.source.TestTrackRemoteDataSource
import com.music.dzr.library.track.domain.model.SavedTrack
import com.music.dzr.library.track.domain.model.TimestampedId
import com.music.dzr.library.track.domain.repository.TrackRepository
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import com.music.dzr.core.error.NetworkError as DomainNetworkError
import com.music.dzr.core.network.dto.error.NetworkError as NetworkErrorDto
import com.music.dzr.core.network.dto.error.NetworkErrorType as NetworkErrorTypeDto

class TrackRepositoryImplTest {

    private lateinit var remoteDataSource: TestTrackRemoteDataSource
    private lateinit var repository: TrackRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatchers = TestDispatcherProvider(testScheduler)
    private val externalScope = ApplicationScope(testDispatchers)

    private val testTrack = networkDetailedTracksTestData.first()

    @BeforeTest
    fun setUp() {
        remoteDataSource = TestTrackRemoteDataSource()
        repository = TrackRepositoryImpl(
            remoteDataSource = remoteDataSource,
            dispatchers = testDispatchers,
            externalScope = externalScope
        )
    }

    @Test
    fun getTrack_returnsSuccess_whenTrackSeeded() = runTest(testScheduler) {
        // Arrange
        val id = "t1"
        remoteDataSource.seedTracks(testTrack.copy(id = id))

        // Act
        val result = repository.getTrack(id, market = Market("US"))

        // Assert
        assertTrue(result.isSuccess())
        assertEquals(id, result.data.id)
    }

    @Test
    fun getMultipleTracks_returnsAllMapped_whenAllSeeded() = runTest(testScheduler) {
        // Arrange
        val a = testTrack.copy(id = "a")
        val b = testTrack.copy(id = "b")
        remoteDataSource.seedTracks(a, b)

        // Act
        val result = repository.getMultipleTracks(listOf("a", "b"), market = Market.Unspecified)

        // Assert
        assertTrue(result.isSuccess())
        val list = result.data
        assertEquals(2, list.size)
        assertEquals("a", list[0].id)
        assertEquals("b", list[1].id)
    }

    @Test
    fun getUserSavedTracks_respectsPagination() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.seedTracks(
            testTrack.copy(id = "1"),
            testTrack.copy(id = "2"),
            testTrack.copy(id = "3")
        )
        repository.saveTracksForUser(listOf("1", "2", "3"))
        val pageable = OffsetPageable(limit = 2, offset = 1)

        // Act
        val result = repository.getUserSavedTracks(pageable)

        // Assert
        assertTrue(result.isSuccess())
        val page = result.data
        assertEquals(2, page.items.size)
        assertEquals("2", page.items[0].music.id)
        assertEquals("3", page.items[1].music.id)
    }

    @Test
    fun saveTracksForUser_setsSavedFlag_andCheckReturnsTrue() = runTest(testScheduler) {
        // Arrange
        val t = testTrack.copy(id = "s1")
        remoteDataSource.seedTracks(t)
        remoteDataSource.saveTimestamp = Instant.fromEpochMilliseconds(1_700_000_000_000)

        val saveRes = repository.saveTracksForUser(listOf("s1"))

        when (saveRes) {
            is Result.Success -> Unit
            is Result.Failure -> fail("Expected Success on saveTracksForUser, got ${saveRes.error}")
        }

        val checkRes = repository.checkUserSavedTracks(listOf("s1"))
        assertTrue(checkRes.isSuccess())
        assertEquals(listOf(true), checkRes.data)

    }

    @Test
    fun saveTracksForUserWithTimestamps_ordersByNewestFirst() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.seedTracks(
            testTrack.copy(id = "ts1"),
            testTrack.copy(id = "ts2")
        )
        val t0 = Instant.parse("2023-01-01T00:00:00Z")
        val t1 = t0 + 10.seconds

        // Act
        val saveResult = repository.saveTracksForUserWithTimestamps(
            listOf(
                TimestampedId(id = "ts1", addedAt = t0),
                TimestampedId(id = "ts2", addedAt = t1)
            )
        )

        val getResult = repository.getUserSavedTracks(market = Market.Unspecified)

        // Assert
        assertTrue(saveResult.isSuccess())
        assertTrue(getResult.isSuccess())
        val page: Page<SavedTrack> = getResult.data
        assertEquals(2, page.items.count())
        assertEquals(t1, page.items[0].addedAt)
        assertEquals(t0, page.items[1].addedAt)
    }


    @Test
    fun removeTracksForUser_clearsSavedFlag() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.seedTracks(testTrack.copy(id = "r1"))
        repository.saveTracksForUser(listOf("r1"))
        testScheduler.advanceUntilIdle()

        // Act & Assert

        val firstCheckResult = repository.checkUserSavedTracks(listOf("r1"))
        assertTrue(firstCheckResult.isSuccess())
        assertEquals(listOf(true), firstCheckResult.data)

        val removeResult = repository.removeTracksForUser(listOf("r1"))
        assertTrue(removeResult.isSuccess())

        val lastCheckResult = repository.checkUserSavedTracks(listOf("r1"))
        assertTrue(lastCheckResult.isSuccess())
        assertEquals(listOf(false), lastCheckResult.data)
    }

    @Test
    fun getTrack_returnsFailure_whenRemoteReturns401() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.forcedError = NetworkErrorDto(
            type = NetworkErrorTypeDto.HttpException,
            message = "Unauthorized",
            code = 401,
            reason = null
        )

        // Act
        val result = repository.getTrack("any", market = Market.Unspecified)

        // Assert
        assertIs<Result.Failure<DomainNetworkError>>(result)
        assertEquals(DomainNetworkError.Unauthorized, result.error)
    }

    private val testArtists = listOf(
        SimplifiedArtist(
            externalUrls = ExternalUrls(
                spotify = "https://open.spotify.com/artist/1dfeR4HaWDbWqFHLkxsg1d"
            ),
            href = "https://api.spotify.com/v1/artists/artist_1",
            id = "artist_1",
            name = "Queen",
            type = "artist",
            uri = "spotify:artist:artist_1"
        )
    )
}