package com.music.dzr.core.auth.data.local

import com.music.dzr.core.auth.data.remote.dto.AuthToken
import com.music.dzr.core.auth.data.security.FakeEncryptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class AuthTokenLocalDataSourceImplTest {

    // Arrange: shared helpers
    private lateinit var dataStore: FakeAuthTokenDataStore
    private lateinit var encryptor: FakeEncryptor
    private lateinit var dataSource: AuthTokenLocalDataSource

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        dataStore = FakeAuthTokenDataStore()
        encryptor = FakeEncryptor()
        dataSource = AuthTokenLocalDataSourceImpl(
            dataStore = dataStore,
            encryptor = encryptor
        )
    }

    @Test
    fun saveToken_savesAndEncryptsAllFields() = runTest(testDispatcher) {
        // Arrange
        val token = AuthToken(
            accessToken = "acc",
            tokenType = "Bearer",
            expiresIn = 3600,
            refreshToken = "ref",
            scope = "user-read"
        )

        // Act
        val isSaved = dataSource.saveToken(token)

        // Assert
        assertTrue(isSaved)

        assertEquals(encryptor.encrypt("acc"), dataStore.getAccessToken())
        assertEquals(encryptor.encrypt("ref"), dataStore.getRefreshToken())
        assertEquals(3600, dataStore.getTokenExpiresIn())
        assertEquals(encryptor.encrypt("user-read"), dataStore.getTokenScope())
        assertEquals(encryptor.encrypt("Bearer"), dataStore.getTokenType())
    }

    @Test
    fun saveToken_preservesOldRefreshTokenWhenNull() = runTest(testDispatcher) {
        // Arrange
        val initial = AuthToken(
            accessToken = "acc1",
            tokenType = "Bearer",
            expiresIn = 1000,
            refreshToken = "ref1",
            scope = null
        )
        dataSource.saveToken(initial)

        val updated = AuthToken(
            accessToken = "acc2",
            tokenType = "Bearer",
            expiresIn = 2000,
            refreshToken = null, // should preserve old
            scope = "scope2"
        )

        // Act
        val isSaved = dataSource.saveToken(updated)

        // Assert
        assertTrue(isSaved)
        assertEquals(encryptor.encrypt("acc2"), dataStore.getAccessToken())
        assertEquals(encryptor.encrypt("ref1"), dataStore.getRefreshToken())
        assertEquals(2000, dataStore.getTokenExpiresIn())
        assertEquals(encryptor.encrypt("scope2"), dataStore.getTokenScope())
        assertEquals(encryptor.encrypt("Bearer"), dataStore.getTokenType())
    }

    @Test
    fun saveToken_removesScopeWhenNull() = runTest(testDispatcher) {
        // Arrange: set a scope first
        val withScope = AuthToken(
            accessToken = "acc",
            tokenType = "Bearer",
            expiresIn = 10,
            refreshToken = "ref",
            scope = "prev-scope"
        )
        dataSource.saveToken(withScope)

        val withoutScope = AuthToken(
            accessToken = "acc",
            tokenType = "Bearer",
            expiresIn = 20,
            refreshToken = null,
            scope = null // should remove key
        )

        // Act
        val isSaved = dataSource.saveToken(withoutScope)

        // Assert
        assertTrue(isSaved)
        assertNull(dataStore.getTokenScope()) // removed
        assertEquals(20, dataStore.getTokenExpiresIn())
    }

    @Test
    fun clearTokens_removesAllKeys() = runTest(testDispatcher) {
        // Arrange
        val token = AuthToken(
            accessToken = "acc",
            tokenType = "Bearer",
            expiresIn = 123,
            refreshToken = "ref",
            scope = "sc"
        )
        dataSource.saveToken(token)

        // Act
        val isCleared = dataSource.clearTokens()

        // Assert
        assertTrue(isCleared)
        assertNull(dataStore.getAccessToken())
        assertNull(dataStore.getRefreshToken())
        assertNull(dataStore.getTokenExpiresIn())
        assertNull(dataStore.getTokenScope())
        assertNull(dataStore.getTokenType())
    }

    @Test
    fun saveToken_returnsFalseOnEncryptorException() = runTest(testDispatcher) {
        // Arrange
        encryptor.failOnEncrypt = true
        val token = AuthToken(
            accessToken = "acc",
            tokenType = "Bearer",
            expiresIn = 1,
            refreshToken = "ref",
            scope = "s"
        )

        // Act
        val isSaved = dataSource.saveToken(token)

        // Assert
        assertFalse(isSaved)
    }
}
