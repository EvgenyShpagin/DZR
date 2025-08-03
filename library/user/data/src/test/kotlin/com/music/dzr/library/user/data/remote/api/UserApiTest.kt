package com.music.dzr.library.user.data.remote.api

import com.music.dzr.core.network.test.createApi
import com.music.dzr.core.network.test.enqueueEmptyResponse
import com.music.dzr.core.network.test.enqueueResponseFromAssets
import com.music.dzr.core.network.model.PlaylistFollowDetails
import com.music.dzr.library.user.data.remote.dto.TimeRange
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: UserApi

    private val id = "1"
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
    fun getCurrentUserProfile_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/my-profile.json")
        // Act
        val response = api.getCurrentUserProfile()
        // Assert
        assertNull(response.error)
        val data = assertNotNull(response.data)
        assertEquals("sancotmdyspqyhjsc2d5qz1du", data.id)
    }

    @Test
    fun getUsersTopArtists_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/top-artists.json")
        // Act
        val response = api.getUsersTopArtists()
        // Assert
        assertNull(response.error)
        val data = assertNotNull(response.data)
        assertEquals(20, data.items.size)
    }

    @Test
    fun getUsersTopTracks_usesCorrectPathAndMethod() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/top-artists.json")
        val timeRange = TimeRange.ShortTerm
        // Act
        api.getUsersTopTracks(limit = 10, offset = 5, timeRange = timeRange)
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals(
            "/me/top/tracks?time_range=${timeRange.urlValue}&limit=10&offset=5",
            recordedRequest.path
        )
        assertEquals("GET", recordedRequest.method)
    }

    @Test
    fun getUserProfile_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/other's-profile.json")
        // Act
        val response = api.getUserProfile(id)
        // Assert
        assertNull(response.error)
        val data = assertNotNull(response.data)
        assertEquals("smedjan", data.displayName)
    }

    @Test
    fun followPlaylist_usesCorrectPathAndMethod() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        val requestBody = PlaylistFollowDetails(public = false)
        // Act
        api.followPlaylist(id, requestBody)
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/playlists/$id/followers", recordedRequest.path)
        assertEquals("PUT", recordedRequest.method)
        assertEquals("""{"public":false}""", recordedRequest.body.readUtf8())
    }

    @Test
    fun unfollowPlaylist_usesCorrectPathAndMethod() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        // Act
        api.unfollowPlaylist(id)
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals("/playlists/$id/followers", recordedRequest.path)
        assertEquals("DELETE", recordedRequest.method)
    }

    @Test
    fun getFollowedArtists_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/followed-artists.json")
        // Act
        val response = api.getFollowedArtists()
        // Assert
        assertNull(response.error)
        val data = assertNotNull(response.data)
        assertEquals(20, data.list.items.size)
    }

    @Test
    fun followArtists_usesCorrectPathAndMethod() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        // Act
        api.followArtists(commaSeparatedIds)
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals(
            "/me/following?type=artist&ids=$encodedCommaSeparatedIds",
            recordedRequest.path
        )
        assertEquals("PUT", recordedRequest.method)
    }

    @Test
    fun unfollowArtists_usesCorrectPathAndMethod() = runTest {
        // Arrange
        server.enqueueEmptyResponse()
        // Act
        api.unfollowArtists(commaSeparatedIds)
        // Assert
        val recordedRequest = server.takeRequest()
        assertEquals(
            "/me/following?type=artist&ids=$encodedCommaSeparatedIds",
            recordedRequest.path
        )
        assertEquals("DELETE", recordedRequest.method)
    }

    @Test
    fun checkIfUserFollowsArtists_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/check-follows-artists-or-users.json")
        // Act
        val response = api.checkIfUserFollowsArtists(commaSeparatedIds)
        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        assertEquals(listOf(false, true), response.data)
    }

    @Test
    fun checkIfUsersFollowPlaylist_returnsData_whenServerRespondsWith200() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/check-current-user-follows-playlist.json")
        // Act
        val response = api.checkIfUsersFollowPlaylist(id)
        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        assertEquals(listOf(true), response.data)
    }
}