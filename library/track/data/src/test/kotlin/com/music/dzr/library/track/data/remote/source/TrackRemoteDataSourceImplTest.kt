package com.music.dzr.library.track.data.remote.source

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.Tracks
import com.music.dzr.library.track.data.remote.api.TrackApi
import com.music.dzr.library.track.data.remote.dto.SaveTracksTimestampedRequest
import com.music.dzr.library.track.data.remote.dto.TimestampedId
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.time.Instant

class TrackRemoteDataSourceImplTest {

    private lateinit var dataSource: TrackRemoteDataSourceImpl
    private lateinit var api: TrackApi

    @BeforeTest
    fun setup() {
        api = mockk()
        dataSource = TrackRemoteDataSourceImpl(api)
    }

    @Test
    fun getMultipleTracks_joinsIdsWithComma_andDelegatesMarket() = runTest {
        // Arrange
        val ids = listOf("1", "2", "3")
        val market = "US"
        val expected = NetworkResponse(data = mockk<Tracks>())
        coEvery { api.getMultipleTracks(ids = "1,2,3", market = market) } returns expected

        // Act
        val actual = dataSource.getMultipleTracks(ids = ids, market = market)

        // Assert
        assertSame(expected, actual)
        coVerify(exactly = 1) { api.getMultipleTracks(ids = "1,2,3", market = market) }
    }

    @Test
    fun checkUsersSavedTracks_joinsIdsWithComma() = runTest {
        // Arrange
        val ids = listOf("a", "b")
        val expected = NetworkResponse(data = listOf(true, false))
        coEvery { api.checkUsersSavedTracks(ids = "a,b") } returns expected

        // Act
        val actual = dataSource.checkUsersSavedTracks(ids)

        // Assert
        assertSame(expected, actual)
        coVerify(exactly = 1) { api.checkUsersSavedTracks(ids = "a,b") }
    }

    @Test
    fun saveTracksForUser_callsQueryOverloadWithList() = runTest {
        // Arrange
        val ids = listOf("1", "2")
        val idsCsv = ids.joinToString(",")
        val expected = NetworkResponse(data = Unit)
        coEvery { api.saveTracksForUser(ids = idsCsv) } returns expected

        // Act
        val actual = dataSource.saveTracksForUser(ids)

        // Assert
        assertSame(expected, actual)
        coVerify(exactly = 1) { api.saveTracksForUser(ids = idsCsv) }
    }

    @Test
    fun saveTracksForUser_callsBodyOverloadWithList() = runTest {
        // Arrange
        val timestampedIds = listOf(TimestampedId("1", Instant.parse("2025-06-07T10:00:00Z")))
        val saveRequest = SaveTracksTimestampedRequest(timestampedIds)
        val expected = NetworkResponse(data = Unit)
        coEvery { api.saveTracksForUser(request = saveRequest) } returns expected

        // Act
        val actual = dataSource.saveTracksForUserWithTimestamps(timestampedIds)

        // Assert
        assertSame(expected, actual)
        coVerify(exactly = 1) { api.saveTracksForUser(saveRequest) }
    }

    @Test
    fun removeTracksForUser_callsBodyOverloadWithList() = runTest {
        // Arrange
        val ids = listOf("1")
        val expected = NetworkResponse(data = Unit)
        coEvery { api.removeTracksForUser(ids = ids) } returns expected

        // Act
        val actual = dataSource.removeTracksForUser(ids)

        // Assert
        assertSame(expected, actual)
        coVerify(exactly = 1) { api.removeTracksForUser(ids = ids) }
    }

    @Test
    fun getTrack_delegatesParams() = runTest {
        // Arrange
        coEvery {
            api.getTrack(id = "11dFghVXANMlKmJXsNCbNl", market = "US")
        } returns NetworkResponse(data = mockk())

        // Act
        dataSource.getTrack(id = "11dFghVXANMlKmJXsNCbNl", market = "US")

        // Assert
        coVerify(exactly = 1) { api.getTrack(id = "11dFghVXANMlKmJXsNCbNl", market = "US") }
    }

    @Test
    fun getUserSavedTracks_delegatesAllOptionalParams() = runTest {
        // Arrange
        coEvery {
            api.getUserSavedTracks(limit = 50, offset = 100, market = "DE")
        } returns NetworkResponse(data = mockk())

        // Act
        dataSource.getUserSavedTracks(limit = 50, offset = 100, market = "DE")

        // Assert
        coVerify(exactly = 1) { api.getUserSavedTracks(limit = 50, offset = 100, market = "DE") }
    }
}