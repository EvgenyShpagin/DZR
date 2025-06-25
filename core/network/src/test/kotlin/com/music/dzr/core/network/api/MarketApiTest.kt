package com.music.dzr.core.network.api

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class MarketApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: MarketApi

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
    fun getMarkets_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange: enqueue a 200 response with a sample album JSON
        server.enqueueResponseFromAssets("responses/market/available-markets.json")

        // Act: call the API
        val response = api.getAvailableMarkets()

        // Assert: correct data is fetched
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals(185, items.count())
            assertEquals("AD", items.first())
            assertEquals("ZW", items.last())
        }
    }

    @Test
    fun getMarkets_usesCorrectPathAndMethod_onRequestWithMarket() = runTest {
        // Arrange: enqueue a 200 response with a sample album JSON
        server.enqueueResponseFromAssets("responses/market/available-markets.json")

        // Act: call the API
        api.getAvailableMarkets()

        // Assert: request path and method
        val recordedRequest = server.takeRequest()
        assertEquals("/markets", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }
}