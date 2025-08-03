package com.music.dzr.library.artist.data.remote.api

import com.music.dzr.core.network.api.createApi
import com.music.dzr.core.network.api.enqueueResponseFromAssets
import com.music.dzr.core.network.model.ReleaseDate
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ArtistApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: ArtistApi
    private val id = "1"
    private val commaSeparatedIds = "1,2,3"
    private val encodedCommaSeparatedIds = "1%2C2%2C3"

    @BeforeTest
    fun setUp() {
        server = MockWebServer().apply { start() }
        api = createApi(server.url("/"))
    }

    @AfterTest
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getArtist_returnsData_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/artist.json")

        // Act
        val response = api.getArtist(id)

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals("Pitbull", name)
            assertEquals(88, popularity)
            assertEquals(emptyList(), genres)
        }
    }

    @Test
    fun getArtist_usesCorrectPathAndMethod_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/artist.json")

        // Act
        api.getArtist(id)

        // Assert Request
        val recorded = server.takeRequest()
        assertEquals("/artists/$id", recorded.path)
        assertEquals("GET", recorded.method)
    }

    @Test
    fun getMultipleArtists_returnsData_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/multiple-artists.json")

        // Act
        val response = api.getMultipleArtists(commaSeparatedIds)

        // Assert Response
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(3, list.count())
            with(list.first()) {
                assertEquals("deadmau5", name)
                assertEquals(68, popularity)
            }
        }
    }

    @Test
    fun getMultipleArtists_usesCorrectPathAndMethod_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/multiple-artists.json")

        // Act
        api.getMultipleArtists(commaSeparatedIds)

        // Assert Request
        val recorded = server.takeRequest()
        assertEquals("/artists?ids=$encodedCommaSeparatedIds", recorded.path)
        assertEquals("GET", recorded.method)
    }

    @Test
    fun getArtistAlbums_returnsData_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/artist-albums.json")

        // Act
        val response = api.getArtistAlbums(id)

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(20, items.count())
            with(items.first()) {
                assertEquals("Trackhouse (Daytona 500 Edition)", name)
                assertEquals(ReleaseDate(2024, 2, 16), releaseDate)
            }
        }
    }

    @Test
    fun getArtistAlbums_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/artist-albums.json")
        val includeGroups = "album,single"
        val encodedIncludeGroups = "album%2Csingle"
        val market = "US"
        val limit = 10
        val offset = 5

        // Act
        api.getArtistAlbums(
            id,
            includeGroups = includeGroups,
            market = market,
            limit = limit,
            offset = offset
        )

        // Assert Request
        val recorded = server.takeRequest()
        val expectedPath = "/artists/$id/albums" +
                "?include_groups=$encodedIncludeGroups&market=$market&limit=$limit&offset=$offset"
        assertEquals(expectedPath, recorded.path)
        assertEquals("GET", recorded.method)
    }

    @Test
    fun getArtistTopTracks_returnsData_onRequestWithMarket() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/artist-top-tracks.json")
        val market = "CA"

        // Act
        val response = api.getArtistTopTracks(id, market)

        // Assert
        assertNull(response.error)
        with(assertNotNull(response.data)) {
            assertEquals(10, list.count())
            with(list.first()) {
                assertEquals("Give Me Everything (feat. Nayer)", name)
                assertEquals("spotify:track:4QNpBfC0zvjKqPJcyqBy9W", uri)
            }
        }
    }

    @Test
    fun getArtistTopTracks_usesCorrectPathAndMethod_onRequestWithMarket() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/artist-top-tracks.json")
        val market = "CA"

        // Act
        api.getArtistTopTracks(id, market)

        // Assert Request
        val recorded = server.takeRequest()
        assertEquals("/artists/$id/top-tracks?market=$market", recorded.path)
        assertEquals("GET", recorded.method)
    }
}
