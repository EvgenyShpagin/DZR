package com.music.dzr.core.network.api

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: UserApi
    private val userId = 1L
    private val playlistId = 2L

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
    fun getCurrentUser_returnsData_whenSuccess() = runTest {
        // API currently doesn't support this feature
    }

    @Test
    fun getUserFavoriteAlbums_returnsList_whenSuccess() = runTest {
        // The API does not currently support this feature
    }

    @Test
    fun getUserFavoriteTracks_returnsList_whenSuccess() = runTest {
        // The API does not currently support this feature
    }

    @Test
    fun getUserPlaylists_returnsList_whenSuccess() = runTest {
        // The API does not currently support this feature
    }

    @Test
    fun getCurrentUser_returnsError_whenGotNon200Code() = runTest {
        // The API does not currently support this feature
    }

    @Test
    fun getCurrentUser_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getCurrentUser()

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/user/me", request.path)
    }

    @Test
    fun createPlaylist_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.createPlaylist(userId, "Test Playlist")

        // then
        val request = server.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/user/$userId/playlists", request.path)
        assertEquals("title=Test%20Playlist", request.body.readUtf8())
    }

    @Test
    fun addTracksToPlaylist_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.addTracksToPlaylist(playlistId, "1,2,3")

        // then
        val request = server.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/playlist/$playlistId/tracks", request.path)
        assertEquals("songs=1%2C2%2C3", request.body.readUtf8())
    }

    @Test
    fun deletePlaylist_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.deletePlaylist(playlistId)

        // then
        val request = server.takeRequest()
        assertEquals("DELETE", request.method)
        assertEquals("/playlist/$playlistId", request.path)
    }

    @Test
    fun removeTracksFromPlaylist_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.removeTracksFromPlaylist(playlistId, "1,2,3")

        // then
        val request = server.takeRequest()
        assertEquals("DELETE", request.method)
        assertEquals("/playlist/$playlistId/tracks?songs=1%2C2%2C3", request.path)
    }
}
