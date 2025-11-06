package com.music.dzr.library.player.data.repository

import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.model.Market
import com.music.dzr.core.testing.assertion.assertFailureEquals
import com.music.dzr.core.testing.assertion.assertSuccess
import com.music.dzr.core.testing.coroutine.TestDispatcherProvider
import com.music.dzr.core.testing.data.networkDetailedTracksTestData
import com.music.dzr.library.player.data.remote.dto.PlayHistory
import com.music.dzr.library.player.data.remote.source.TestPlayerRemoteDataSource
import com.music.dzr.player.domain.model.RecentlyPlayedFilter
import com.music.dzr.player.domain.model.RepeatMode
import com.music.dzr.player.domain.model.TargetDevice
import com.music.dzr.player.domain.repository.PlayerRepository
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant
import com.music.dzr.core.error.NetworkError as DomainNetworkError
import com.music.dzr.core.network.dto.error.NetworkError as NetworkErrorDto
import com.music.dzr.core.network.dto.error.NetworkErrorType as NetworkErrorTypeDto

class PlayerRepositoryImplTest {

    private lateinit var remoteDataSource: TestPlayerRemoteDataSource
    private lateinit var repository: PlayerRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatchers = TestDispatcherProvider(testScheduler)
    private val externalScope = ApplicationScope(testDispatchers)

    @BeforeTest
    fun setUp() {
        remoteDataSource = TestPlayerRemoteDataSource()
        repository = PlayerRepositoryImpl(
            remoteDataSource = remoteDataSource,
            dispatchers = testDispatchers,
            externalScope = externalScope
        )
    }

    @Test
    fun getPlaybackState_returnsSuccess() = runTest(testScheduler) {
        // Arrange
        val market: Market = Market.Unspecified

        // Act
        val result = repository.getPlaybackState(market = market)

        // Assert
        assertSuccess(result)
    }

    @Test
    fun getAvailableDevices_returnsNonEmptyList() = runTest(testScheduler) {
        // Arrange
        // (defaults from fake already have one active device)

        // Act
        val result = repository.getAvailableDevices()

        // Assert
        assertSuccess(result)
        assertTrue(result.data.isNotEmpty())
    }

    @Test
    fun startPauseSeek_succeeds() = runTest(testScheduler) {
        // Arrange
        val device: TargetDevice = TargetDevice.Current
        val newPosition = 1_234

        // Act
        val startRes = repository.startOrResumePlayback(device = device)
        val pauseRes = repository.pausePlayback(device = device)
        val seekRes = repository.seekToPosition(positionMs = newPosition, device = device)

        // Assert
        assertSuccess(startRes)
        assertSuccess(pauseRes)
        assertSuccess(seekRes)
    }

    @Test
    fun setShuffleRepeatVolume_succeeds() = runTest(testScheduler) {
        // Arrange
        val device: TargetDevice = TargetDevice.Current
        val shuffleEnabled = true
        val repeat = RepeatMode.Off
        val volume = 75

        // Act
        val shuffleRes = repository.setShuffle(enabled = shuffleEnabled, device = device)
        val repeatRes = repository.setRepeatMode(mode = repeat, device = device)
        val volumeRes = repository.setPlaybackVolume(volumePercent = volume, device = device)

        // Assert
        assertSuccess(shuffleRes)
        assertSuccess(repeatRes)
        assertSuccess(volumeRes)
    }

    @Test
    fun getRecentlyPlayed_returnsMappedPage() = runTest(testScheduler) {
        // Arrange
        val track = networkDetailedTracksTestData.first()
        remoteDataSource.recentlyPlayed.add(
            PlayHistory(
                track = track,
                playedAt = Instant.fromEpochMilliseconds(1_700_000_000_000),
                context = null
            )
        )
        val filter = RecentlyPlayedFilter.Default

        // Act
        val result = repository.getRecentlyPlayed(filter)

        // Assert
        assertSuccess(result)
        assertEquals(1, result.data.items.size)
    }

    @Test
    fun getQueue_returnsItems() = runTest(testScheduler) {
        // Arrange
        val track = networkDetailedTracksTestData.first()
        remoteDataSource.queueTracks.add(track)

        // Act
        val result = repository.getQueue()

        // Assert
        assertSuccess(result)
        assertEquals(1, result.data.upcoming.size)
    }

    @Test
    fun addToQueue_succeeds() = runTest(testScheduler) {
        // Arrange
        val device: TargetDevice = TargetDevice.Current
        val trackId = "track_123"

        // Act
        val result = repository.addToQueue(
            trackId = trackId,
            device = device
        )

        // Assert
        assertSuccess(result)
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
        val market: Market = Market.Unspecified

        // Act
        val result = repository.getPlaybackState(market = market)

        // Assert
        assertFailureEquals(DomainNetworkError.Unauthorized, result)
    }
}
