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

class RadioApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: RadioApi
    private val radioId = 1L // dummy id

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
    fun getRadios_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("radios_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getRadios()

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!.data) {
            assertEquals(93, count())
            with(first()) {
                assertEquals("The '80s", title)
                assertEquals("radio", type)
            }
        }
    }

    @Test
    fun getRadio_returnsData_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("radio_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getRadio(radioId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals("Electro", title)
            assertEquals("radio", type)
        }
    }

    @Test
    fun getRadioGenres_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("radio_genres_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getRadioGenres()

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!.data) {
            assertEquals(21, count())
            with(first()) {
                assertEquals("Pop", title)
                assertEquals(13, radios.count())
            }
        }
    }

    @Test
    fun getTopRadios_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("radio_top_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getTopRadios()

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!.data) {
            assertEquals(25, count())
            with(first()) {
                assertEquals("The '80s", title)
                assertEquals("radio", type)
            }
        }
    }

    @Test
    fun getRadioTracks_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("radio_tracks_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getRadioTracks(radioId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        with(response.data!!.data) {
            assertEquals(25, count())
            with(first()) {
                assertEquals("Vox populi (Original)", title)
                assertEquals(false, readable)
            }
        }
    }

    @Test
    fun getRadioLists_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("radio_lists_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getRadioLists()

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!.data) {
            assertEquals(25, count())
            with(first()) {
                assertEquals("Country", title)
                assertEquals("radio", type)
            }
        }
    }

    @Test
    fun getRadio_returnsError_whenGotNon200Code() = runTest {
        // given
        val body = getJsonBodyAsset("no_data_error_response_body.json")
        server.enqueue(MockResponse().setResponseCode(404).setBody(body))

        // when
        val response = api.getRadio(-1)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
        assertEquals(NetworkErrorType.DataException, response.error!!.type)
    }

    @Test
    fun getRadios_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getRadios()

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/radio", request.path)
    }

    @Test
    fun getRadio_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getRadio(radioId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/radio/$radioId", request.path)
    }

    @Test
    fun getRadioTracks_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getRadioTracks(radioId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/radio/$radioId/tracks", request.path)
    }

    @Test
    fun getRadioGenres_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getRadioGenres()

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/radio/genres", request.path)
    }

    @Test
    fun getRadioLists_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getRadioLists()

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/radio/lists", request.path)
    }
}