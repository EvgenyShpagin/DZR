package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.NetworkErrorType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class ChartApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: ChartApi
    private val genreId = 1L // dummy id

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
    fun getCharts_returnsData_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("charts_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getCharts()

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertTrue(tracks.data.isNotEmpty())
            assertTrue(artists.data.isNotEmpty())
            assertTrue(albums.data.isNotEmpty())
            assertTrue(playlists.data.isNotEmpty())
        }
    }

    @Test
    fun getTopTracks_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("charts_top_tracks_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getTopTracks(genreId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(10, list.count())
        assertEquals("Hell N Back (feat. Summer Walker)", list[0].title)
    }

    @Test
    fun getTopAlbums_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("charts_top_albums_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getTopAlbums(genreId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(10, list.count())
        assertEquals("Something Beautiful", list[0].title)
    }

    @Test
    fun getTopArtists_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("charts_top_artists_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getTopArtists(genreId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(10, list.count())
        assertEquals("Kanye West", list[0].name)
    }

    @Test
    fun getTopPlaylists_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("charts_top_playlists_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getTopPlaylists(genreId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(10, list.count())
        assertEquals("Piano Meditation", list[0].title)
    }

    @Test
    fun getCharts_returnsError_whenGotNon200Code() = runTest {
        // given
        val body = getJsonBodyAsset("no_data_error_response_body.json")
        server.enqueue(MockResponse().setResponseCode(500).setBody(body))

        // when
        val response = api.getCharts()

        // then
        assertNull(response.data)
        assertNotNull(response.error)
        assertEquals(NetworkErrorType.DataException, response.error!!.type)
    }

    @Test
    fun getCharts_returnsError_whenInvalidJson() = runTest {
        // given
        val body = "invalid response"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getCharts()

        // then
        assertNull(response.data)
        assertNotNull(response.error)
    }

    @Test
    fun getCharts_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        val body = "{}"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        api.getCharts()

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/chart", request.path)
    }

    @Test
    fun getTopTracks_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getTopTracks(genreId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/chart/$genreId/tracks", request.path)
    }

    @Test
    fun getTopTracks_withDefaultGenre_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getTopTracks()

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/chart/0/tracks", request.path)
    }

    @Test
    fun getTopAlbums_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getTopAlbums(genreId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/chart/$genreId/albums", request.path)
    }

    @Test
    fun getTopArtists_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getTopArtists(genreId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/chart/$genreId/artists", request.path)
    }

    @Test
    fun getTopPlaylists_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getTopPlaylists(genreId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/chart/$genreId/playlists", request.path)
    }
}