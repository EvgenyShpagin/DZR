package com.music.dzr.library.playlist.data.remote

import com.music.dzr.core.network.createApi
import com.music.dzr.core.network.enqueueEmptyResponse
import com.music.dzr.core.network.enqueueResponseFromAssets
import com.music.dzr.library.playlist.data.remote.api.PlaylistApi
import com.music.dzr.library.playlist.data.remote.dto.NewPlaylistDetails
import com.music.dzr.library.playlist.data.remote.dto.PlaylistDetailsUpdate
import com.music.dzr.library.playlist.data.remote.dto.PlaylistItemsUpdate
import com.music.dzr.library.playlist.data.remote.dto.TrackAdditions
import com.music.dzr.library.playlist.data.remote.dto.TrackRemovals
import com.music.dzr.library.playlist.data.remote.dto.TrackToRemove
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PlaylistApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: PlaylistApi
    private val json = Json.Default

    // Dummy parameters
    private val playlistId = "3cEYpjA9oz9GiPac4AsH4n"
    private val userId = "dummy_id"
    private val trackUris = listOf("spotify:track:4iV5W9uYEdYUVa79Axb7Rh")
    private val trackUri = trackUris.first()

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
    fun getPlaylist_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/playlist.json")

        // Act
        val response = api.getPlaylist(playlistId)

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
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
        server.enqueueResponseFromAssets("responses/playlist.json")

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
        val changeDetails = PlaylistDetailsUpdate(name = "New Name")
        val response = api.changePlaylistDetails(playlistId, changeDetails)

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun changePlaylistDetails_usesCorrectPathMethodAndBody_onRequest() = runTest {
        // Arrange
        server.enqueueEmptyResponse(204)
        val requestBody = PlaylistDetailsUpdate(
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
        server.enqueueResponseFromAssets("responses/playlist-tracks.json")

        // Act
        val response = api.getPlaylistTracks(playlistId)

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(5, total)
            with(items) {
                assertEquals("Api", first().track.name)
            }
        }
    }

    @Test
    fun getPlaylistTracks_usesCorrectPathAndMethod_onRequestWithParams() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/playlist-tracks.json")

        // Act
        api.getPlaylistTracks(playlistId, market = "ES", limit = 10, offset = 5)

        // Assert
        val request = server.takeRequest()
        assertEquals("/playlists/$playlistId/tracks?market=ES&limit=10&offset=5", request.path)
        assertEquals("GET", request.method)
    }

    @Test
    fun updatePlaylistTracks_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/snapshot-id.json")
        val requestBody = PlaylistItemsUpdate(rangeStart = 0, insertBefore = 2)

        // Act
        val response = api.updatePlaylistTracks(playlistId, requestBody)

        // Assert
        assertNull(response.error)
        val data = assertNotNull(response.data)
        assertEquals("JbtmHBDBAYu3/bt8BOXKbBTGCxgNZz3JJX6sfZGjC", data.snapshotId)
    }

    @Test
    fun updatePlaylistTracks_usesCorrectPathMethodAndBody_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/snapshot-id.json")
        val requestBody = PlaylistItemsUpdate(
            rangeStart = 0,
            insertBefore = 2,
            uris = listOf("spotify:track:4iV5W9uYEdYUVa79Axb7Rh")
        )

        // Act
        api.updatePlaylistTracks(playlistId, requestBody)

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/playlists/$playlistId/tracks", recordedRequest.path)

        val expectedBody = json.encodeToString(requestBody)
        val actualBody = recordedRequest.body.readUtf8()
        assertEquals(json.parseToJsonElement(expectedBody), json.parseToJsonElement(actualBody))
    }

    @Test
    fun addTracksToPlaylist_returnsData_on201CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/snapshot-id.json", 201)
        val requestBody = TrackAdditions(uris = trackUris, position = 0)

        // Act
        val response = api.addTracksToPlaylist(playlistId, requestBody)

        // Assert
        assertNull(response.error)
        val data = assertNotNull(response.data)
        assertEquals("JbtmHBDBAYu3/bt8BOXKbBTGCxgNZz3JJX6sfZGjC", data.snapshotId)
    }

    @Test
    fun addTracksToPlaylist_usesCorrectPathMethodAndBody_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/snapshot-id.json", 201)
        val requestBody = TrackAdditions(uris = trackUris, position = 0)

        // Act
        api.addTracksToPlaylist(playlistId, requestBody)

        // Assert
        val request = server.takeRequest()
        assertEquals("/playlists/$playlistId/tracks", request.path)
        assertEquals("POST", request.method)
        assertEquals(json.encodeToString(requestBody), request.body.readUtf8())
    }

    @Test
    fun removePlaylistTracks_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/snapshot-id.json")
        val requestBody = TrackRemovals(tracks = listOf(TrackToRemove(trackUri)))

        // Act
        val response = api.removePlaylistTracks(playlistId, requestBody)

        // Assert
        assertNull(response.error)
        val data = assertNotNull(response.data)
        assertEquals("JbtmHBDBAYu3/bt8BOXKbBTGCxgNZz3JJX6sfZGjC", data.snapshotId)
    }

    @Test
    fun removePlaylistTracks_usesCorrectPathMethodAndBody_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/snapshot-id.json")
        val requestBody = TrackRemovals(tracks = listOf(TrackToRemove(trackUri)))

        // Act
        api.removePlaylistTracks(playlistId, requestBody)

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/playlists/$playlistId/tracks", recordedRequest.path)

        val expectedBody = json.encodeToString(requestBody)
        val actualBody = recordedRequest.body.readUtf8()
        assertEquals(json.parseToJsonElement(expectedBody), json.parseToJsonElement(actualBody))
    }

    @Test
    fun getCurrentUserPlaylists_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/user-playlists.json")

        // Act
        val response = api.getCurrentUserPlaylists()

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(9, total)
            assertEquals("Rock", items.first().name)
        }
    }

    @Test
    fun getCurrentUserPlaylists_usesCorrectPathAndMethod_onRequest() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        // Act
        api.getCurrentUserPlaylists()

        // Assert
        val request = server.takeRequest()
        assertEquals("/me/playlists", request.path)
        assertEquals("GET", request.method)
    }

    @Test // As they act the same way
    fun getUserPlaylists_returnsData_on200CodeResponse() =
        getCurrentUserPlaylists_returnsData_on200CodeResponse()

    @Test
    fun getUserPlaylists_usesCorrectPathAndMethod_onRequestWithParams() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        // Act
        api.getUserPlaylists(userId, limit = 3, offset = 2)

        // Assert
        val request = server.takeRequest()
        assertEquals("/users/$userId/playlists?limit=3&offset=2", request.path)
        assertEquals("GET", request.method)
    }

    @Test
    fun createPlaylist_returnsData_on201CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/playlist.json", 201)
        val requestBody = NewPlaylistDetails(name = "Spotify Web API Testing playlist")

        // Act
        val response = api.createPlaylist(userId, requestBody)

        // Assert
        assertNull(response.error)
        val data = assertNotNull(response.data)
        assertEquals("3cEYpjA9oz9GiPac4AsH4n", data.id)
    }

    @Test
    fun createPlaylist_usesCorrectPathMethodAndBody_onRequest() = runTest {
        // Arrange
        server.enqueueEmptyResponse(201)
        val requestBody = NewPlaylistDetails(
            name = "Spotify Web API Testing playlist",
            public = false,
            collaborative = true,
            description = "Test description"
        )

        // Act
        api.createPlaylist(userId, requestBody)

        // Assert
        val request = server.takeRequest()
        assertEquals("/users/$userId/playlists", request.path)
        assertEquals("POST", request.method)
        assertEquals(json.encodeToString(requestBody), request.body.readUtf8())
    }

    @Test
    fun getPlaylistCoverImage_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/playlist-cover-images.json")

        // Act
        val response = api.getPlaylistCoverImage(playlistId)

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(3, count())
            assertEquals(640, first().height)
        }
    }

    @Test
    fun getPlaylistCoverImage_usesCorrectPathAndMethod_onRequest() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        // Act
        api.getPlaylistCoverImage(playlistId)

        // Assert
        val request = server.takeRequest()
        assertEquals("/playlists/$playlistId/images", request.path)
        assertEquals("GET", request.method)
    }

    @Test
    fun uploadCustomPlaylistCover_isSuccessful_on202Response() = runTest {
        // Arrange
        server.enqueueEmptyResponse(202)
        val imageBody = "jpeg_image_data".toRequestBody("image/jpeg".toMediaType())

        // Act
        val response = api.uploadCustomPlaylistCover(playlistId, imageBody)

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun uploadCustomPlaylistCover_usesCorrectPathMethodAndBody_onRequest() = runTest {
        // Arrange
        server.enqueueEmptyResponse(202)
        val imageBody = "jpeg_image_data".toRequestBody("image/jpeg".toMediaType())

        // Act
        api.uploadCustomPlaylistCover(playlistId, imageBody)

        // Assert
        val request = server.takeRequest()
        assertEquals("/playlists/$playlistId/images", request.path)
        assertEquals("PUT", request.method)
        assertEquals("jpeg_image_data", request.body.readUtf8())
    }
}