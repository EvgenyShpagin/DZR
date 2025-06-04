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

class SearchApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: SearchApi
    private val query = "eminem" // dummy query

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
    fun searchTracks_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("search_tracks_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.searchTracks(query)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!.data) {
            assertEquals(25, count())
            with(first()) {
                assertEquals("Without Me", title)
                assertEquals(961431, rank)
            }
        }
    }

    @Test
    fun searchAlbums_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("search_albums_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.searchAlbums(query)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!.data) {
            assertEquals(25, count())
            with(first()) {
                assertEquals("The Eminem Show", title)
                assertEquals(20, nbTracks)
                assertEquals(116, genreId)
            }
        }
    }

    @Test
    fun searchArtists_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("search_artists_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.searchArtists(query)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!.data) {
            assertEquals(25, count())
            with(first()) {
                assertEquals("Eminem", name)
                assertEquals(68, nbAlbum)
                assertEquals(18255254, nbFan)
                assertEquals("artist", type)
            }
        }
    }

    @Test
    fun searchPlaylists_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("search_playlists_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.searchPlaylists(query)

        // then
        with(response.data!!.data) {
            assertEquals(25, count())
            with(first()) {
                assertEquals("100% Eminem", title)
                assertEquals(40, nbTracks)
                assertEquals(true, public)
                assertEquals("playlist", type)
            }
        }
    }

    @Test
    fun searchRadios_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("search_radios_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.searchRadios("work")

        // then
        with(response.data!!.data) {
            assertEquals(2, count())
            with(first()) {
                assertEquals("Work Out", title)
                assertEquals("radio", type)
            }
        }
    }

    @Test
    fun searchTracks_returnsError_whenGotNon200Code() = runTest {
        // given
        val body = getJsonBodyAsset("no_data_error_response_body.json")
        server.enqueue(MockResponse().setResponseCode(400).setBody(body))

        // when
        val response = api.searchTracks("")

        // then
        assertNull(response.data)
        assertNotNull(response.error)
        assertEquals(NetworkErrorType.DataException, response.error!!.type)
    }

    @Test
    fun searchTracks_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.searchTracks(query)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/search/track?q=eminem", request.path)
    }

    @Test
    fun searchAlbums_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.searchAlbums(query)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/search/album?q=eminem", request.path)
    }

    @Test
    fun searchArtists_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.searchArtists(query)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/search/artist?q=eminem", request.path)
    }
}
