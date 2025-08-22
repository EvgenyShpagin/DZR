package com.music.dzr.library.track.data.remote.api

import com.music.dzr.core.network.test.createApi
import com.music.dzr.core.network.test.enqueueEmptyResponse
import com.music.dzr.core.network.test.enqueueResponseFromAssets
import com.music.dzr.core.network.test.toJsonArray
import com.music.dzr.library.track.data.remote.dto.SaveTracksTimestampedRequest
import com.music.dzr.library.track.data.remote.dto.TimestampedId
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.*
import kotlin.time.Instant

class TrackApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: TrackApi

    private val id = "1"
    private val saveTracksTimestampedRequest = SaveTracksTimestampedRequest(
        timestampedIds = listOf(
            TimestampedId("1", Instant.parse("2025-06-07T12:34:56Z")),
            TimestampedId("2", Instant.parse("2025-06-07T12:34:57Z")),
            TimestampedId("3", Instant.parse("2025-06-07T12:34:58Z")),
        )
    )
    private val ids = listOf("1", "2", "3")
    private val commaSeparatedIds = "1,2,3"
    private val encodedCommaSeparatedIds = "1%2C2%2C3"

    @BeforeTest
    fun setUp() {
        server = MockWebServer()
        server.start()
        api = createApi(server.url("/"))
    }

    @AfterTest
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getTrack_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/track.json")
        // Act
        val response = api.getTrack(id)
        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals("11dFghVXANMlKmJXsNCbNl", id)
            assertEquals("Cut To The Feeling", name)
        }
    }

    @Test
    fun getTrack_usesCorrectPathAndMethod_onRequestWithMarket() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/track.json")
        // Act
        api.getTrack(id, market = "US")
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/tracks/$id?market=US", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }

    @Test
    fun getMultipleTracks_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/multiple-tracks.json")
        // Act
        val response = api.getMultipleTracks(commaSeparatedIds)
        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(3, list.count())
            assertEquals("Knights of Cydonia", list.first().name)
        }
    }

    @Test
    fun getMultipleTracks_usesCorrectPathAndMethod_onRequestWithMarket() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/multiple-tracks.json")
        // Act
        api.getMultipleTracks(commaSeparatedIds, market = "GB")
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/tracks?ids=$encodedCommaSeparatedIds&market=GB", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }

    @Test
    fun getUserSavedTracks_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/user-saved-tracks.json")
        // Act
        val response = api.getUserSavedTracks()
        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(3, items.count())
            with(items.first()) {
                assertEquals(Instant.parse("2025-06-14T13:45:57Z"), addedAt)
                assertEquals("1BKT2I9x4RGKaKqW4up34s", track.id)
            }
        }
    }

    @Test
    fun getUserSavedTracks_usesCorrectPathAndMethod_onResponseWithParams() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/user-saved-tracks.json")
        // Act
        api.getUserSavedTracks(limit = 20, offset = 0, market = "FR")
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/me/tracks?limit=20&offset=0&market=FR", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }

    @Test
    fun saveTracksForUser_returnsData_on200CodeResponseWithQuery() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        // Act
        val response = api.saveTracksForUser(commaSeparatedIds)
        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun saveTracksForUser_usesCorrectPathAndMethod_onRequestWithQuery() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        // Act
        api.saveTracksForUser(commaSeparatedIds)
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/me/tracks?ids=$encodedCommaSeparatedIds", recordedRequest.path)
        assertEquals("PUT", recordedRequest.method)
    }

    @Test
    fun saveTracksForUser_returnsData_on200CodeResponseWithBody() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        // Act
        val response = api.saveTracksForUser(saveTracksTimestampedRequest)
        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun saveTracksForUser_usesCorrectBodyAndMethod_onRequestWithBody() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        // Act
        api.saveTracksForUser(saveTracksTimestampedRequest)
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/me/tracks", recordedRequest.path)
        assertEquals("PUT", recordedRequest.method)
        with(recordedRequest.body.readUtf8()) {
            assertTrue("\"1\"" in this && "\"2025-06-07T12:34:56Z\"" in this)
            assertTrue("\"2\"" in this && "\"2025-06-07T12:34:57Z\"" in this)
            assertTrue("\"3\"" in this && "\"2025-06-07T12:34:58Z\"" in this)
        }
    }

    @Test
    fun removeTracksForUser_returnsData_on200CodeResponseWithQuery() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        // Act
        val response = api.removeTracksForUser(commaSeparatedIds)
        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun removeTracksForUser_usesCorrectPathAndMethod_onRequestWithQuery() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        // Act
        api.removeTracksForUser(commaSeparatedIds)
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/me/tracks?ids=$encodedCommaSeparatedIds", recordedRequest.path)
        assertEquals("DELETE", recordedRequest.method)
    }

    @Test
    fun removeTracksForUser_returnsData_on200CodeResponseWithBody() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        // Act
        val response = api.removeTracksForUser(ids)
        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun removeTracksForUser_usesCorrectBodyAndMethod_onRequestWithBody() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        // Act
        api.removeTracksForUser(ids)
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/me/tracks", recordedRequest.path)
        assertEquals("DELETE", recordedRequest.method)
        assertEquals(ids.toJsonArray(), recordedRequest.body.readUtf8())
    }

    @Test
    fun checkUserSavedTracks_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/check-user-saved-tracks.json")
        // Act
        val response = api.checkUserSavedTracks(commaSeparatedIds)
        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        assertEquals(listOf(false, true), response.data)
    }

    @Test
    fun checkUserSavedTracks_usesCorrectPathAndMethod_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/check-user-saved-tracks.json")
        // Act
        api.checkUserSavedTracks(commaSeparatedIds)
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/me/tracks/contains?ids=$encodedCommaSeparatedIds", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }
}