package com.music.dzr.feature.search.data.remote.api

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

class SearchApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: SearchApi

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
    fun search_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/search-results.json")

        // Act
        val response = api.search(query = "dummy", type = "dummy")

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertNotNull(tracks)
            assertEquals(20, tracks.limit)
            assertEquals(829, tracks.total)
            with(tracks.items.first()) {
                assertEquals("superman (eminem, dina rae) [sped up version]", album.name)
                assertEquals("superman (eminem, dina rae) - sped up version", name)
            }
        }
    }

    @Test
    fun search_usesCorrectPathAndMethod_onRequestWithAllParameters() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/search-results.json")
        val spaceEncodedQuery = "remaster%20track:Doxy%20artist:Miles%20Davis"
        val encodedQuery = "remaster%2520track%3ADoxy%2520artist%3AMiles%2520Davis"
        val type = "album,track"
        val encodedType = "album%2Ctrack"
        val market = "US"
        val limit = 10
        val offset = 5
        val includeExternal = "audio"

        // Act
        api.search(
            query = spaceEncodedQuery,
            type = type,
            market = market,
            limit = limit,
            offset = offset,
            includeExternal = includeExternal
        )

        // Assert
        val recordedRequest = server.takeRequest()
        val expectedPath = "/search?q=$encodedQuery" +
                "&type=$encodedType" +
                "&market=$market" +
                "&limit=$limit" +
                "&offset=$offset" +
                "&include_external=$includeExternal"
        assertEquals(expectedPath, recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }
}