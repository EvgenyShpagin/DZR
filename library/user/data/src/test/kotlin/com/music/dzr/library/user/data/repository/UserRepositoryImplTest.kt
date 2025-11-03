package com.music.dzr.library.user.data.repository

import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.error.ConnectivityError
import com.music.dzr.core.network.dto.Followers
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.network.dto.error.NetworkErrorType
import com.music.dzr.core.pagination.CursorPageable
import com.music.dzr.core.pagination.OffsetPageable
import com.music.dzr.core.testing.assertion.assertFailureEquals
import com.music.dzr.core.testing.assertion.assertSuccess
import com.music.dzr.core.testing.assertion.assertSuccessEquals
import com.music.dzr.core.testing.coroutine.TestDispatcherProvider
import com.music.dzr.core.testing.data.networkDetailedTracksTestData
import com.music.dzr.library.user.data.remote.source.TestUserRemoteDataSource
import com.music.dzr.library.user.domain.repository.UserRepository
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.music.dzr.core.error.NetworkError as CoreNetworkError
import com.music.dzr.core.network.dto.Artist as NetworkArtist
import com.music.dzr.core.network.dto.ExternalUrls as NetworkExternalUrls

internal class UserRepositoryImplTest {

    private lateinit var remoteDataSource: TestUserRemoteDataSource
    private lateinit var repository: UserRepository

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatchers = TestDispatcherProvider(testScheduler)

    @BeforeTest
    fun setUp() {
        remoteDataSource = TestUserRemoteDataSource()
        repository = UserRepositoryImpl(
            remoteDataSource = remoteDataSource,
            dispatchers = testDispatchers,
            externalScope = ApplicationScope(testDispatchers)
        )
    }

    @Test
    fun getCurrentUserProfile_returnsUser_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.getCurrentUserProfile()

        // Assert
        assertSuccess(result)
        assertEquals("test_user", result.data.id)
        assertEquals("Test User", result.data.displayName)
    }

    @Test
    fun getCurrentUserProfile_returnsError_whenRemoteFails() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.forcedError =
            NetworkError(NetworkErrorType.Timeout, "Request Timeout", 408)

        // Act
        val result = repository.getCurrentUserProfile()

        // Assert
        assertFailureEquals(ConnectivityError.Timeout, result)
    }

    @Test
    fun getUserTopArtists_returnsMappedPage_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange
        val paginatedList = PaginatedList(
            items = listOf(networkArtist),
            href = "",
            limit = 1,
            offset = 0,
            next = null,
            previous = null,
            total = 1
        )
        remoteDataSource.userTopArtists = paginatedList
        val pageable = OffsetPageable(limit = 1, offset = 0)

        // Act
        val result = repository.getUserTopArtists(pageable = pageable)

        // Assert
        assertSuccess(result)
        assertEquals(1, result.data.items.size)
        assertEquals("artist1", result.data.items[0].id)
        assertEquals("Artist 1", result.data.items[0].name)
    }

    @Test
    fun getUserTopArtists_returnsError_whenRemoteFails() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.forcedError = NetworkError(NetworkErrorType.HttpException, "", 403)
        val pageable = OffsetPageable(limit = 1, offset = 0)

        // Act
        val result = repository.getUserTopArtists(pageable = pageable)

        // Assert
        assertFailureEquals(CoreNetworkError.InsufficientPermissions, result)
    }

    @Test
    fun getUserTopTracks_returnsMappedPage_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange
        val paginatedList = PaginatedList(
            items = listOf(networkDetailedTracksTestData.first()),
            href = "",
            limit = 1,
            offset = 0,
            next = null,
            previous = null,
            total = 1
        )
        remoteDataSource.userTopTracks = paginatedList
        val pageable = OffsetPageable(limit = 1, offset = 0)

        // Act
        val result = repository.getUserTopTracks(pageable = pageable)

        // Assert
        assertSuccess(result)
        assertEquals(1, result.data.items.size)
        assertEquals("track_1", result.data.items[0].id)
        assertEquals("Bohemian Rhapsody", result.data.items[0].name)
    }

    @Test
    fun getUserTopTracks_returnsError_whenRemoteFails() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.forcedError = NetworkError(NetworkErrorType.HttpException, "", 500)
        val pageable = OffsetPageable(limit = 1, offset = 0)

        // Act
        val result = repository.getUserTopTracks(pageable = pageable)

        // Assert
        assertFailureEquals(CoreNetworkError.ServerError, result)
    }

    @Test
    fun getUserProfile_returnsMappedUser_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.getUserProfile("public_user_123")

        // Assert
        assertSuccess(result)
        assertEquals("public_user_123", result.data.id)
        assertEquals("Public User", result.data.displayName)
    }

    @Test
    fun getUserProfile_returnsError_whenRemoteFails() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.forcedError = NetworkError(NetworkErrorType.HttpException, "", 500)

        // Act
        val result = repository.getUserProfile("non_existent_user")

        // Assert
        assertFailureEquals(CoreNetworkError.ServerError, result)
    }

    @Test
    fun followPlaylist_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.followPlaylist("playlist1", true)

        // Assert
        assertSuccess(result)
    }

    @Test
    fun unfollowPlaylist_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.unfollowPlaylist("playlist1")

        // Assert
        assertSuccess(result)
    }

    @Test
    fun getFollowedArtists_returnsMappedPage_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange
        val pageable = CursorPageable(limit = 1, cursor = null)

        // Act
        val result = repository.getFollowedArtists(pageable)

        // Assert
        assertSuccess(result)
        assertTrue(result.data.items.isEmpty()) // Fake returns an empty list
    }

    @Test
    fun followArtists_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.followArtists(listOf("artist1"))

        // Assert
        assertSuccess(result)
    }

    @Test
    fun unfollowArtists_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.unfollowArtists(listOf("artist1"))

        // Assert
        assertSuccess(result)
    }

    @Test
    fun checkIfUserFollowsArtists_returnsBooleanList_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.checkIfUserFollowsArtists(listOf("artist1", "artist2"))

        // Assert
        assertSuccessEquals(listOf(false, false), result) // Fake returns false for all ids;
    }

    @Test
    fun followUsers_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.followUsers(listOf("user1"))

        // Assert
        assertSuccess(result)
    }

    @Test
    fun unfollowUsers_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.unfollowUsers(listOf("user1"))

        // Assert
        assertSuccess(result)
    }

    @Test
    fun checkIfUserFollowsUsers_returnsBooleanList_whenRemoteSucceeds() = runTest(testScheduler) {

        // Arrange

        // Act
        val result = repository.checkIfUserFollowsUsers(listOf("user1", "user2"))

        // Assert
        assertSuccessEquals(listOf(false, false), result) // Fake returns false for all ids
    }

    @Test
    fun checkIfUsersFollowPlaylist_returnsBoolean_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.checkIfUsersFollowPlaylist("playlist1")

        // Assert
        assertSuccessEquals(false, result) // Fake returns false
    }

    val networkArtist = NetworkArtist(
        id = "artist1",
        name = "Artist 1",
        type = "artist",
        href = "",
        uri = "",
        externalUrls = NetworkExternalUrls(""),
        followers = Followers("", 0),
        genres = emptyList(),
        images = emptyList(),
        popularity = 0
    )
}
