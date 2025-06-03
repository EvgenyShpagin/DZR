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

class GenreApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: GenreApi
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
    fun getGenres_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("genres_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getGenres()

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(28, list.count())
        assertEquals("All", list[0].name)
    }

    @Test
    fun getGenre_returnsData_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("genre_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getGenre(genreId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals("All", name)
            assertEquals("genre", type)
        }
    }

    @Test
    fun getGenreArtists_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("genre_artists_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getGenreArtists(genreId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(42, list.count())
        assertEquals("Kanye West", list[0].name)
    }

    @Test
    fun getGenreRadios_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("genre_radios_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getGenreRadios(genreId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(89, list.count())
        assertEquals("Blues", list[0].title)
    }

    @Test
    fun getGenre_returnsError_whenGotNon200Code() = runTest {
        // given
        val body = getJsonBodyAsset("no_data_error_response_body.json")
        server.enqueue(MockResponse().setResponseCode(404).setBody(body))

        // when
        val response = api.getGenre(-1)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
        assertEquals(NetworkErrorType.DataException, response.error!!.type)
    }

    @Test
    fun getGenre_returnsError_whenInvalidJson() = runTest {
        // given
        val body = "invalid response"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getGenre(genreId)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
    }

    @Test
    fun getGenres_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        val body = "{}"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        api.getGenres()

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/genre", request.path)
    }

    @Test
    fun getGenre_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        val body = "{}"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        api.getGenre(genreId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/genre/$genreId", request.path)
    }

    @Test
    fun getGenreArtists_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getGenreArtists(genreId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/genre/$genreId/artists", request.path)
    }

    @Test
    fun getGenreRadios_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getGenreRadios(genreId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/genre/$genreId/radios", request.path)
    }
}