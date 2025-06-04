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

class TrackApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: TrackApi
    private val trackId = 1L // dummy id

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
    fun getTrack_returnsData_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("track_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getTrack(trackId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals("Harder, Better, Faster, Stronger", title)
            assertEquals(226, duration)
            assertEquals(806178, rank)
            assertEquals(1, contributors.count())
        }
    }

    @Test
    fun getTrack_returnsError_whenGotNon200Code() = runTest {
        // given
        val body = getJsonBodyAsset("no_data_error_response_body.json")
        server.enqueue(MockResponse().setResponseCode(404).setBody(body))

        // when
        val response = api.getTrack(-1)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
        assertEquals(NetworkErrorType.DataException, response.error!!.type)
    }

    @Test
    fun getTrack_returnsError_whenInvalidJson() = runTest {
        // given
        val body = "invalid response"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getTrack(trackId)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
    }

    @Test
    fun getTrack_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getTrack(trackId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/track/$trackId", request.path)
    }
}