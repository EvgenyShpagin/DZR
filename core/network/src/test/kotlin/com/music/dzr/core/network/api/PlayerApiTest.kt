package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.Offset
import com.music.dzr.core.network.model.PlaybackOptions
import com.music.dzr.core.network.model.RepeatMode
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class PlayerApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: PlayerApi
    private val deviceId = "dummy_id"
    private val market = "US"

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
        api = createApi(server.url("/"))
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getPlaybackState_returnsData_when200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("playback_state_response.json")

        // Act
        val response = api.getPlaybackState()

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        assertEquals(true, response.data!!.isPlaying)
    }

    @Test
    fun getPlaybackState_usesCorrectPathAndMethod_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("playback_state_response.json")

        // Act
        api.getPlaybackState(market)

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player?market=$market", recorded.path)
        assertEquals("GET", recorded.method)
    }

    @Test
    fun transferPlayback_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        val deviceIds = mapOf("device_ids" to listOf(deviceId))
        val play = true

        // Act
        api.transferPlayback(deviceIds, play)

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player?play=$play", recorded.path)
        assertEquals("PUT", recorded.method)
        assertEquals("""{"device_ids":["$deviceId"]}""", recorded.body.readUtf8())
    }

    @Test
    fun getAvailableDevices_returnsData_when200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("available_devices_response.json")

        // Act
        val response = api.getAvailableDevices()

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        assertEquals(2, response.data!!.devices.size)
        assertEquals("febce7d2eaa377ac85b9f2289f744806ce55435b", response.data!!.devices[0].id)
    }

    @Test
    fun getAvailableDevices_usesCorrectPathAndMethod_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("available_devices_response.json")

        // Act
        api.getAvailableDevices()

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player/devices", recorded.path)
        assertEquals("GET", recorded.method)
    }

    @Test
    fun getCurrentlyPlayingTrack_returnsData_when200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("currently_playing_track_response.json")

        // Act
        val response = api.getCurrentlyPlayingTrack(market)

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        assertEquals(true, response.data!!.isPlaying)
    }

    @Test
    fun getCurrentlyPlayingTrack_usesCorrectPathAndMethod_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("currently_playing_track_response.json")

        // Act
        api.getCurrentlyPlayingTrack(market)

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player/currently-playing?market=$market", recorded.path)
        assertEquals("GET", recorded.method)
    }

    @Test
    fun startOrResumePlayback_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        val options = PlaybackOptions(
            contextUri = "spotify:album:1Je1IMUlBXcx1Fz0WE7oPT",
            uris = listOf(
                "spotify:track:4iV5W9uYEdYUVa79Axb7Rh",
                "spotify:track:1301WleyT98MSxVHPZCA6M"
            ),
            offset = Offset.ByPosition(position = 5),
            positionMs = 10000
        )

        // Act
        api.startOrResumePlayback(deviceId, options)

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player/play?device_id=$deviceId", recorded.path)
        assertEquals("PUT", recorded.method)
        val expectedBody =
            """{"context_uri":"spotify:album:1Je1IMUlBXcx1Fz0WE7oPT","uris":["spotify:track:4iV5W9uYEdYUVa79Axb7Rh","spotify:track:1301WleyT98MSxVHPZCA6M"],"offset":{"position":5},"position_ms":10000}"""
        assertEquals(expectedBody, recorded.body.readUtf8())
    }

    @Test
    fun pausePlayback_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        // Act
        api.pausePlayback(deviceId)

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player/pause?device_id=$deviceId", recorded.path)
        assertEquals("PUT", recorded.method)
    }

    @Test
    fun skipToNext_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        // Act
        api.skipToNext(deviceId)

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player/next?device_id=$deviceId", recorded.path)
        assertEquals("POST", recorded.method)
    }

    @Test
    fun skipToPrevious_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        // Act
        api.skipToPrevious(deviceId)

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player/previous?device_id=$deviceId", recorded.path)
        assertEquals("POST", recorded.method)
    }

    @Test
    fun seekToPosition_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        val positionMs = 60000

        // Act
        api.seekToPosition(positionMs, deviceId)

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player/seek?position_ms=$positionMs&device_id=$deviceId", recorded.path)
        assertEquals("PUT", recorded.method)
    }

    @Test
    fun setRepeatMode_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        val state = RepeatMode.Context

        // Act
        api.setRepeatMode(state, deviceId)

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player/repeat?state=$state&device_id=$deviceId", recorded.path)
        assertEquals("PUT", recorded.method)
    }

    @Test
    fun setPlaybackVolume_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        val volumePercent = 50

        // Act
        api.setPlaybackVolume(volumePercent, deviceId)

        // Assert
        val recorded = server.takeRequest()
        assertEquals(
            "/me/player/volume?volume_percent=$volumePercent&device_id=$deviceId",
            recorded.path
        )
        assertEquals("PUT", recorded.method)
    }

    @Test
    fun toggleShuffle_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        val state = true

        // Act
        api.toggleShuffle(state, deviceId)

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player/shuffle?state=$state&device_id=$deviceId", recorded.path)
        assertEquals("PUT", recorded.method)
    }

    @Test
    fun getRecentlyPlayed_returnsData_when200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("recently_played_response.json")

        // Act
        val response = api.getRecentlyPlayed()

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        assertEquals(20, response.data!!.items.size)
    }

    @Test
    fun getRecentlyPlayed_usesCorrectPathAndMethod_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("recently_played_response.json")
        val limit = 10
        val after = 1234567890L

        // Act
        api.getRecentlyPlayed(limit = limit, after = after)

        // Assert Request
        val recorded = server.takeRequest()
        assertEquals("/me/player/recently-played?limit=$limit&after=$after", recorded.path)
        assertEquals("GET", recorded.method)
    }

    @Test
    fun getUserQueue_returnsData_when200CodeResponse() = runTest {
        // Arrange
        val json = """{"currently_playing": null, "queue": []}"""
        server.enqueue(MockResponse().setBody(json))

        // Act
        val response = api.getUserQueue()

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun getUserQueue_usesCorrectPathAndMethod_onRequest() = runTest {
        // Arrange
        server.enqueue(MockResponse().setBody("{}"))

        // Act
        api.getUserQueue()

        // Assert
        val recorded = server.takeRequest()
        assertEquals("/me/player/queue", recorded.path)
        assertEquals("GET", recorded.method)
    }

    @Test
    fun addToQueue_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        val uri = "spotify:track:test_uri"

        // Act
        api.addToQueue(uri, deviceId)

        // Assert
        val recorded = server.takeRequest()
        val encodedUri = "spotify%3Atrack%3Atest_uri"
        assertEquals("/me/player/queue?uri=$encodedUri&device_id=$deviceId", recorded.path)
        assertEquals("POST", recorded.method)
    }
} 