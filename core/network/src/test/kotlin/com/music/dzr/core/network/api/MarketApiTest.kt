package com.music.dzr.core.network.api

import com.music.dzr.core.network.test.createApi
import com.music.dzr.core.network.test.enqueueResponseFromAssets
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MarketApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: MarketApi

    @BeforeTest
    fun setUp() {
        server = MockWebServer()
        server.start()
        api = createApi(server.url("/"))
    }

    @AfterTest
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
        with(response.data) {
            assertEquals(185, list.count())
            assertEquals("AD", list.first())
            assertEquals("ZW", list.last())
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