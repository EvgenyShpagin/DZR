package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.NetworkErrorType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class AlbumApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: AlbumApi
    private val albumId = 1L // dummy id

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
    fun getAlbum_returnsData_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("album_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getAlbum(albumId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals("724384960650", upc)
            assertEquals("Discovery", title)
            assertEquals(14, nbTracks)
        }
    }

    @Test
    fun getAlbumTracks_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("album_tracks_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getAlbumTracks(albumId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(14, list.count())
        assertEquals("One More Time", list[0].title)
        assertEquals(320, list[0].duration)
    }

    @Test
    fun getAlbum_returnsError_whenGotNon200Code() = runTest {
        // given
        val body = getJsonBodyAsset("no_data_error_response_body.json")
        server.enqueue(MockResponse().setResponseCode(404).setBody(body))

        // when
        val response = api.getAlbum(-1)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
        assertEquals(NetworkErrorType.DataException, response.error!!.type)
    }

    @Test
    fun getAlbum_returnsError_whenInvalidJson() = runTest {
        // given
        val body = "invalid response"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))
        // when
        val response = api.getAlbum(albumId)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
    }

    @Test
    fun getAlbum_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        val body = "{}"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        api.getAlbum(albumId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/album/$albumId", request.path)
    }

    @Test
    fun getAlbumTracks_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getAlbumTracks(albumId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/album/$albumId/tracks", request.path)
    }
}