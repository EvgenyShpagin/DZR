package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.NetworkErrorType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class PlaylistApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: PlaylistApi
    private val playlistId = 1L // dummy id

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
    fun getPlaylist_returnsData_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("playlist_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getPlaylist(playlistId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals("En mode 60", title)
            assertEquals(49, nbTracks)
            assertEquals(LocalDateTime.parse("2014-06-27T05:09:31"), this.creationDate)
            assertEquals(19390, fans)
        }
    }

    @Test
    fun getPlaylistTracks_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("playlist_tracks_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getPlaylistTracks(playlistId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(25, list.count())
        assertEquals("Hey Jude (Remastered 2015)", list[0].title)
        assertEquals(429, list[0].duration)
        assertEquals(837001, list[0].rank)
    }

    @Test
    fun getPlaylist_returnsError_whenGotNon200Code() = runTest {
        // given
        val body = getJsonBodyAsset("no_data_error_response_body.json")
        server.enqueue(MockResponse().setResponseCode(404).setBody(body))

        // when
        val response = api.getPlaylist(-1)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
        assertEquals(NetworkErrorType.DataException, response.error!!.type)
    }

    @Test
    fun getPlaylist_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getPlaylist(playlistId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/playlist/$playlistId", request.path)
    }

    @Test
    fun getPlaylistTracks_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getPlaylistTracks(playlistId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/playlist/$playlistId/tracks", request.path)
    }
}