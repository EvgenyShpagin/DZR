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

class EditorialApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: EditorialApi
    private val editorialId = 1L // dummy id

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
    fun getEditorials_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("editorials_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getEditorials()

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(25, list.count())
        assertEquals("All", list[0].name)
    }

    @Test
    fun getEditorial_returnsData_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("editorial_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getEditorial(editorialId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals("All", name)
        }
    }

    @Test
    fun getEditorialSelection_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("editorial_selection_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getEditorialSelection(editorialId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(10, list.count())
        assertEquals("Origins Of Aggression", list[0].title)
    }

    @Test
    fun getEditorialReleases_returnsList_whenSuccess() = runTest {
        // given
        val body = getJsonBodyAsset("editorial_releases_response_body.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getEditorialReleases(editorialId)

        // then
        assertNull(response.error)
        assertNotNull(response.data)
        val list = response.data!!.data
        assertEquals(20, list.count())
        assertEquals("MERCON2BABIÈRE", list[0].title)
    }

    @Test
    fun getEditorial_returnsError_whenGotNon200Code() = runTest {
        // given
        val body = getJsonBodyAsset("no_data_error_response_body.json")
        server.enqueue(MockResponse().setResponseCode(404).setBody(body))

        // when
        val response = api.getEditorial(-1)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
        assertEquals(NetworkErrorType.DataException, response.error!!.type)
    }

    @Test
    fun getEditorial_returnsError_whenInvalidJson() = runTest {
        // given
        val body = "invalid response"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        val response = api.getEditorial(editorialId)

        // then
        assertNull(response.data)
        assertNotNull(response.error)
    }

    @Test
    fun getEditorials_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        val body = "{}"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        api.getEditorials()

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/editorial", request.path)
    }

    @Test
    fun getEditorial_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        val body = "{}"
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        // when
        api.getEditorial(editorialId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/editorial/$editorialId", request.path)
    }

    @Test
    fun getEditorialSelection_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getEditorialSelection(editorialId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/editorial/$editorialId/selection", request.path)
    }

    @Test
    fun getEditorialReleases_usesCorrectPathAndMethod_onRequest() = runTest {
        // given
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // when
        api.getEditorialReleases(editorialId)

        // then
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/editorial/$editorialId/releases", request.path)
    }
}