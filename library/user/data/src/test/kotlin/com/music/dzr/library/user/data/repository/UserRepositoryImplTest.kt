package com.music.dzr.library.user.data.repository

import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.error.ConnectivityError
import com.music.dzr.core.network.dto.Followers
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.network.dto.error.NetworkErrorType
import com.music.dzr.core.result.Result
import com.music.dzr.core.testing.coroutine.TestDispatcherProvider
import com.music.dzr.core.testing.data.networkDetailedTracksTestData
import com.music.dzr.library.user.data.remote.source.FakeUserRemoteDataSource
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import com.music.dzr.core.error.NetworkError as CoreNetworkError
import com.music.dzr.core.network.dto.Artist as NetworkArtist
import com.music.dzr.core.network.dto.ExternalUrls as NetworkExternalUrls

internal class UserRepositoryImplTest {

    private lateinit var remoteDataSource: FakeUserRemoteDataSource
    private lateinit var repository: UserRepositoryImpl

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatchers = TestDispatcherProvider(testScheduler)

    @BeforeTest
    fun setUp() {
        remoteDataSource = FakeUserRemoteDataSource()
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
        assertIs<Result.Success<*>>(result)
        val user = (result as Result.Success).data
        assertEquals("test_user", user.id)
        assertEquals("Test User", user.displayName)
    }

    @Test
    fun getCurrentUserProfile_returnsError_whenRemoteFails() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.forcedError =
            NetworkError(NetworkErrorType.Timeout, "Request Timeout", 408)

        // Act
        val result = repository.getCurrentUserProfile()

        // Assert
        assertIs<Result.Failure<*>>(result)
        val appError = result.error
        assertEquals(ConnectivityError.Timeout, appError)
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

        // Act
        val result = repository.getUserTopArtists(limit = 1, offset = 0)

        // Assert
        assertIs<Result.Success<*>>(result)
        val page = (result as Result.Success).data
        assertEquals(1, page.items.size)
        assertEquals("artist1", page.items[0].id)
        assertEquals("Artist 1", page.items[0].name)
    }

    @Test
    fun getUserTopArtists_returnsError_whenRemoteFails() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.forcedError = NetworkError(NetworkErrorType.HttpException, "", 403)

        // Act
        val result = repository.getUserTopArtists(limit = 1, offset = 0)

        // Assert
        assertIs<Result.Failure<*>>(result)
        val appError = result.error
        assertEquals(CoreNetworkError.InsufficientPermissions, appError)
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

        // Act
        val result = repository.getUserTopTracks(limit = 1, offset = 0)

        // Assert
        assertIs<Result.Success<*>>(result)
        val page = (result as Result.Success).data
        assertEquals(1, page.items.size)
        assertEquals("track_1", page.items[0].id)
        assertEquals("Bohemian Rhapsody", page.items[0].name)
    }

    @Test
    fun getUserTopTracks_returnsError_whenRemoteFails() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.forcedError = NetworkError(NetworkErrorType.HttpException, "", 500)

        // Act
        val result = repository.getUserTopTracks(limit = 1, offset = 0)

        // Assert
        assertIs<Result.Failure<*>>(result)
        val appError = result.error
        assertEquals(CoreNetworkError.ServerError, appError)
    }

    @Test
    fun getUserProfile_returnsMappedUser_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.getUserProfile("public_user_123")

        // Assert
        assertIs<Result.Success<*>>(result)
        val user = (result as Result.Success).data
        assertEquals("public_user_123", user.id)
        assertEquals("Public User", user.displayName)
    }

    @Test
    fun getUserProfile_returnsError_whenRemoteFails() = runTest(testScheduler) {
        // Arrange
        remoteDataSource.forcedError = NetworkError(NetworkErrorType.HttpException, "", 500)

        // Act
        val result = repository.getUserProfile("non_existent_user")

        // Assert
        assertIs<Result.Failure<*>>(result)
        val appError = result.error
        assertEquals(CoreNetworkError.ServerError, appError)
    }

    @Test
    fun followPlaylist_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.followPlaylist("playlist1", true)

        // Assert
        assertIs<Result.Success<*>>(result)
    }

    @Test
    fun unfollowPlaylist_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.unfollowPlaylist("playlist1")

        // Assert
        assertIs<Result.Success<*>>(result)
    }

    @Test
    fun getFollowedArtists_returnsMappedPage_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.getFollowedArtists(limit = 1)

        // Assert
        assertIs<Result.Success<*>>(result)
        val page = (result as Result.Success).data
        assertTrue(page.items.isEmpty()) // Fake returns an empty list
    }

    @Test
    fun followArtists_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.followArtists(listOf("artist1"))

        // Assert
        assertIs<Result.Success<*>>(result)
    }

    @Test
    fun unfollowArtists_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.unfollowArtists(listOf("artist1"))

        // Assert
        assertIs<Result.Success<*>>(result)
    }

    @Test
    fun checkIfUserFollowsArtists_returnsBooleanList_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.checkIfUserFollowsArtists(listOf("artist1", "artist2"))

        // Assert
        assertIs<Result.Success<*>>(result)
        val follows = (result as Result.Success).data
        assertEquals(listOf(false, false), follows) // Fake returns false for all ids
    }

    @Test
    fun followUsers_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.followUsers(listOf("user1"))

        // Assert
        assertIs<Result.Success<*>>(result)
    }

    @Test
    fun unfollowUsers_returnsSuccess_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.unfollowUsers(listOf("user1"))

        // Assert
        assertIs<Result.Success<*>>(result)
    }

    @Test
    fun checkIfUserFollowsUsers_returnsBooleanList_whenRemoteSucceeds() = runTest(testScheduler) {

        // Arrange

        // Act
        val result = repository.checkIfUserFollowsUsers(listOf("user1", "user2"))

        // Assert
        assertIs<Result.Success<*>>(result)
        val follows = (result as Result.Success).data
        assertEquals(listOf(false, false), follows) // Fake returns false for all ids
    }

    @Test
    fun checkIfUsersFollowPlaylist_returnsBoolean_whenRemoteSucceeds() = runTest(testScheduler) {
        // Arrange

        // Act
        val result = repository.checkIfUsersFollowPlaylist("playlist1")

        // Assert
        assertIs<Result.Success<Boolean>>(result)
        val follows = result.data
        assertEquals(false, follows) // Fake returns false
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
