package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.ChangePlaylistDetailsRequest
import com.music.dzr.core.network.model.UpdatePlaylistItemsRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PlaylistApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: PlaylistApi
    private val json = Json

    // Dummy parameters
    private val playlistId = "3cEYpjA9oz9GiPac4AsH4n"

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        api = createApi(server.url("/"))
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getPlaylist_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("playlist-responses/playlist.json")

        // Act
        val response = api.getPlaylist(playlistId)

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals("Spotify Web API Testing playlist", name)
            with(tracks) {
                assertEquals("Api", items.first().track.name)
                assertEquals(5, this.total)
            }
        }
    }

    @Test
    fun getPlaylist_usesCorrectPathAndMethod_onRequestWithParams() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("playlist-responses/playlist.json")

        // Act
        api.getPlaylist(playlistId, market = "US", fields = "name,description")

        // Assert
        val request = server.takeRequest()
        assertEquals("/playlists/$playlistId?market=US&fields=name%2Cdescription", request.path)
        assertEquals("GET", request.method)
    }

    @Test
    fun changePlaylistDetails_isSuccessful_on204Response() = runTest {
        // Arrange
        server.enqueueEmptyResponse(204)

        // Act
        val changeDetails = ChangePlaylistDetailsRequest(name = "New Name")
        val response = api.changePlaylistDetails(playlistId, changeDetails)

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun changePlaylistDetails_usesCorrectPathMethodAndBody_onRequest() = runTest {
        // Arrange
        server.enqueueEmptyResponse(204)
        val requestBody = ChangePlaylistDetailsRequest(
            name = "New Playlist Name",
            public = true,
            collaborative = false,
            description = "New description"
        )

        // Act
        api.changePlaylistDetails(playlistId, requestBody)

        // Assert
        val request = server.takeRequest()
        assertEquals("/playlists/$playlistId", request.path)
        assertEquals("PUT", request.method)
        assertEquals(json.encodeToString(requestBody), request.body.readUtf8())
    }

    @Test
    fun getPlaylistTracks_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("playlist-responses/playlist-tracks.json")

        // Act
        val response = api.getPlaylistTracks(playlistId)

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals(5, total)
            with(items) {
                assertEquals("Api", first().track.name)
            }
        }
    }

    @Test
    fun getPlaylistTracks_usesCorrectPathAndMethod_onRequestWithParams() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("playlist-responses/playlist-tracks.json")

        // Act
        api.getPlaylistTracks(playlistId, market = "ES", limit = 10, offset = 5)

        // Assert
        val request = server.takeRequest()
        assertEquals("/playlists/$playlistId/tracks?market=ES&limit=10&offset=5", request.path)
        assertEquals("GET", request.method)
    }

    @Test
    fun updatePlaylistItems_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("playlist-responses/snapshot-id.json")
        val requestBody = UpdatePlaylistItemsRequest(rangeStart = 0, insertBefore = 2)

        // Act
        val response = api.updatePlaylistItems(playlistId, requestBody)

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        assertEquals("JbtmHBDBAYu3/bt8BOXKbBTGCxgNZz3JJX6sfZGjC", response.data!!.snapshotId)
    }

    @Test
    fun updatePlaylistItems_usesCorrectPathMethodAndBody_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("playlist-responses/snapshot-id.json")
        val requestBody = UpdatePlaylistItemsRequest(
            rangeStart = 0,
            insertBefore = 2,
            uris = listOf("spotify:track:4iV5W9uYEdYUVa79Axb7Rh")
        )

        // Act
        api.updatePlaylistItems(playlistId, requestBody)

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/playlists/$playlistId/tracks", recordedRequest.path)

        val expectedBody = json.encodeToString(requestBody)
        val actualBody = recordedRequest.body.readUtf8()
        assertEquals(json.parseToJsonElement(expectedBody), json.parseToJsonElement(actualBody))
    }

} 