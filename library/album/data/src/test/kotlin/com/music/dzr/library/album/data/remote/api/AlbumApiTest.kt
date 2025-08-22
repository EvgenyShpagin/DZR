package com.music.dzr.library.album.data.remote.api

import com.music.dzr.core.network.test.createApi
import com.music.dzr.core.network.test.enqueueEmptyResponse
import com.music.dzr.core.network.test.enqueueResponseFromAssets
import com.music.dzr.core.network.test.toJsonArray
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Instant

class AlbumApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: AlbumApi

    // Dummy parameters (ignored by MockWebServer)
    private val id = "1"
    private val idList = listOf("1", "2", "3")
    private val commaSeparatedIds = "1,2,3"
    private val encodedCommaSeparatedIds = "1%2C2%2C3"

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
    fun getAlbum_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange: enqueue a 200 response with a sample album JSON
        server.enqueueResponseFromAssets("responses/album.json")

        // Act: call the API (no market parameter)
        val response = api.getAlbum(id)

        // Assert: correct data is fetched
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(3, images.count())
            assertEquals(18, tracks.total)
            assertEquals("Global Warming", name)
        }
    }

    @Test
    fun getAlbum_usesCorrectPathAndMethod_onRequestWithMarket() = runTest {
        // Arrange: enqueue a 200 response with a sample album JSON
        server.enqueueResponseFromAssets("responses/album.json")

        // Act: call the API (has market parameter)
        api.getAlbum(id, market = "US")

        // Assert: request path and method
        val recordedRequest = server.takeRequest()
        assertEquals("/albums/$id?market=US", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }

    @Test
    fun getMultipleAlbums_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/multiple-albums.json")

        // Act
        val response = api.getMultipleAlbums(commaSeparatedIds)

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(3, list.count())
            assertEquals("TRON: Legacy Reconfigured", list.first().name)
        }
    }

    @Test
    fun getMultipleAlbums_usesCorrectPathAndMethod_onRequestWithMarket() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/multiple-albums.json")

        // Act
        api.getMultipleAlbums(commaSeparatedIds, market = "GB")

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/albums?ids=$encodedCommaSeparatedIds&market=GB", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }

    @Test
    fun getAlbumTracks_returnsData_whenServerRespondsWith200() = runTest {
        // mockResponse
        server.enqueueResponseFromAssets("responses/album-tracks.json")

        // Act
        val response = api.getAlbumTracks(id)

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(18, items.count())
            with(items.first()) {
                assertEquals("Global Warming (feat. Sensato)", name)
                assertEquals(true, explicit)
            }
        }
    }

    @Test
    fun getAlbumTracks_usesCorrectPathAndMethod_onResponseWithParams() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/album-tracks.json")

        // Act
        api.getAlbumTracks(id, market = "CA", limit = 5, offset = 10)

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals(
            "/albums/$id/tracks?market=CA&limit=5&offset=10",
            recordedRequest.path
        )
        assertEquals("GET", recordedRequest.method)
    }

    @Test
    fun getUserSavedAlbums_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/user-saved-albums.json")

        // Act
        val response = api.getUserSavedAlbums()

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(2, items.count())
            with(items.first()) {
                assertEquals("TRON: Legacy Reconfigured", album.name)
                assertEquals(Instant.parse("2025-06-07T12:34:56Z"), addedAt)
            }
        }
    }

    @Test
    fun getUserSavedAlbums_usesCorrectPathAndMethod_onResponseWithParams() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/user-saved-albums.json")

        // Act
        api.getUserSavedAlbums(limit = 20, offset = 0, market = "FR")

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/me/albums?limit=20&offset=0&market=FR", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }

    @Test
    fun saveAlbumsForUser_returnsData_on200CodeResponseWithQuery() = runTest {
        // Arrange: server returns 200 OK with empty body
        server.enqueueEmptyResponse()

        // Act
        val response = api.saveAlbumsForUser(commaSeparatedIds)

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun saveAlbumsForUser_usesCorrectPathAndMethod_onRequestWithQuery() = runTest {
        // Arrange: server returns 200 OK with empty body
        server.enqueueEmptyResponse()

        // Act
        api.saveAlbumsForUser(commaSeparatedIds)

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/me/albums?ids=$encodedCommaSeparatedIds", recordedRequest.path)
        assertEquals("PUT", recordedRequest.method)
    }

    @Test
    fun saveAlbumsForUser_returnsData_on200CodeResponseWithBody() = runTest {
        // Arrange: server returns 200 OK with empty body
        server.enqueueEmptyResponse()

        // Act
        val response = api.saveAlbumsForUser(idList)

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun saveAlbumsForUser_usesCorrectBodyAndMethod_onRequestWithBody() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        // Act
        api.saveAlbumsForUser(idList)

        // Verify that the request body contains JSON array of IDs
        val recordedRequest = server.takeRequest()
        val sentBody = recordedRequest.body.readUtf8()
        assertEquals(idList.toJsonArray(), sentBody)
        assertEquals("PUT", recordedRequest.method)
    }

    @Test
    fun removeAlbumsForUser_returnsData_on200CodeResponseWithQuery() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        // Act
        val response = api.removeAlbumsForUser(commaSeparatedIds)

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun removeAlbumsForUser_usesCorrectBodyAndMethod_onRequestWithQuery() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        // Act
        api.removeAlbumsForUser(commaSeparatedIds)

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/me/albums?ids=$encodedCommaSeparatedIds", recordedRequest.path)
        assertEquals("DELETE", recordedRequest.method)
    }

    @Test
    fun removeAlbumsForUser_returnsData_on200CodeResponseWithBody() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        // Act
        val response = api.removeAlbumsForUser(idList)

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
    }

    @Test
    fun removeAlbumsForUser_usesCorrectBodyAndMethod_onRequestWithBody() = runTest {
        // Arrange
        server.enqueueEmptyResponse()

        // Act
        api.removeAlbumsForUser(idList)

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/me/albums", recordedRequest.path)
        assertEquals("DELETE", recordedRequest.method)
        val sentBody = recordedRequest.body.readUtf8()
        assertEquals(idList.toJsonArray(), sentBody)
    }

    @Test
    fun checkUsersSavedAlbums_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/check-user-saved-albums.json")

        // Act
        val response = api.checkUsersSavedAlbums(commaSeparatedIds)

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(2, size)
            assertEquals(false, first())
        }
    }

    @Test
    fun checkUsersSavedAlbums_usesCorrectPathAndMethod_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/check-user-saved-albums.json")

        // Act
        api.checkUsersSavedAlbums(commaSeparatedIds)

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/me/albums/contains?ids=$encodedCommaSeparatedIds", recordedRequest.path)
    }

    @Test
    fun getNewReleases_returnsData_on200CodeResponse() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/new-releases.json")

        // Act
        val response = api.getNewReleases()

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(20, list.items.count())
            assertEquals(20, list.limit)
            assertEquals(100, list.total)
        }
    }

    @Test
    fun getNewReleases_usesCorrectPathAndMethod_onRequestWithParams() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/new-releases.json")

        // Act
        api.getNewReleases(limit = 10, offset = 5)

        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/browse/new-releases?limit=10&offset=5", recordedRequest.path)
    }
}
