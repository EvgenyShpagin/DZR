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

class ArtistApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: ArtistApi
    private val artistId = 1L // dummy id

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
    fun getArtist_returnsData_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("artist_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getArtist(artistId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals("Daft Punk", name)
            assertEquals(4901570, nbFan)
            assertEquals(38, nbAlbum)
        }
    }

    @Test
    fun getArtistTopTracks_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("artist_top_tracks_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getArtistTopTracks(artistId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(5, list.count())
        assertEquals("Instant Crush (feat. Julian Casablancas)", list[0].title)
    }

    @Test
    fun getArtistAlbums_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("artist_albums_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getArtistAlbums(artistId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(25, list.count())
        assertEquals("Homework (25th Anniversary Edition)", list[0].title)
    }

    @Test
    fun getRelatedArtists_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("artist_related_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getRelatedArtists(artistId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(20, list.count())
        assertEquals("Justice", list[0].name)
    }

    @Test
    fun getArtistRadioTracks_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("artist_radio_tracks_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getArtistRadioTracks(artistId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(25, list.count())
        assertEquals("Digital Love", list[0].title)
    }

    @Test
    fun getArtistPlaylists_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("artist_playlists_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getArtistPlaylists(artistId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(10, list.count())
        assertEquals("Marvel Rock", list[0].title)
    }

    @Test
    fun getArtist_returnsError_whenGotNon200Code() = runTest {
        // given
        val body = getJsonBodyAsset("no_data_error_response_body.json")
        server.enqueue(MockResponse().setResponseCode(404).setBody(body))

        // when
        val response = api.getArtist(-1)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
        assertEquals(NetworkErrorType.DataException, response.error!!.type)
    }

    @Test
    fun getArtist_returnsError_whenInvalidJson() = runTest {
        // given
        val body = "invalid response"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getArtist(artistId)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
    }

    @Test
    fun getArtist_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        val body = "{}"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        api.getArtist(artistId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/artist/$artistId", request.path)
    }

    @Test
    fun getArtistTopTracks_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getArtistTopTracks(artistId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/artist/$artistId/top", request.path)
    }

    @Test
    fun getArtistAlbums_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getArtistAlbums(artistId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/artist/$artistId/albums", request.path)
    }

    @Test
    fun getRelatedArtists_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getRelatedArtists(artistId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/artist/$artistId/related", request.path)
    }

    @Test
    fun getArtistRadioTracks_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getArtistRadioTracks(artistId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/artist/$artistId/radio", request.path)
    }

    @Test
    fun getArtistPlaylists_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getArtistPlaylists(artistId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/artist/$artistId/playlists", request.path)
    }
}