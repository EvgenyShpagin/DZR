package com.music.dzr.core.network.api

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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

    // Dummy parameters
    private val playlistId = "dummy_id"

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

} 