package com.music.dzr.core.auth.data.local.source

import com.music.dzr.core.auth.data.local.security.FakeEncryptor
import com.music.dzr.core.auth.data.remote.dto.AuthToken
import com.music.dzr.core.result.isSuccess
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
    fun save_savesAndEncryptsAllFields() = runTest(testDispatcher) {
        // Arrange
        val token = AuthToken(
            accessToken = "acc",
            tokenType = "Bearer",
            expiresIn = 3600,
            refreshToken = "ref",
            scope = "user-read"
        )

        // Act
        val saveResult = dataSource.save(token)

        // Assert
        assertTrue(saveResult.isSuccess())

        assertEquals(encryptor.encrypt("acc"), dataStore.getAccessToken())
        assertEquals(encryptor.encrypt("ref"), dataStore.getRefreshToken())
        assertEquals(3600, dataStore.getTokenExpiresIn())
        assertEquals(encryptor.encrypt("user-read"), dataStore.getTokenScope())
        assertEquals(encryptor.encrypt("Bearer"), dataStore.getTokenType())
    }

    @Test
    fun save_preservesOldRefreshTokenWhenNull() = runTest(testDispatcher) {
        // Arrange
        val initial = AuthToken(
            accessToken = "acc1",
            tokenType = "Bearer",
            expiresIn = 1000,
            refreshToken = "ref1",
            scope = null
        )
        dataSource.save(initial)

        val updated = AuthToken(
            accessToken = "acc2",
            tokenType = "Bearer",
            expiresIn = 2000,
            refreshToken = null, // should preserve old
            scope = "scope2"
        )

        // Act
        val saveResult = dataSource.save(updated)

        // Assert
        assertTrue(saveResult.isSuccess())
        assertEquals(encryptor.encrypt("acc2"), dataStore.getAccessToken())
        assertEquals(encryptor.encrypt("ref1"), dataStore.getRefreshToken())
        assertEquals(2000, dataStore.getTokenExpiresIn())
        assertEquals(encryptor.encrypt("scope2"), dataStore.getTokenScope())
        assertEquals(encryptor.encrypt("Bearer"), dataStore.getTokenType())
    }

    @Test
    fun save_removesScopeWhenNull() = runTest(testDispatcher) {
        // Arrange: set a scope first
        val withScope = AuthToken(
            accessToken = "acc",
            tokenType = "Bearer",
            expiresIn = 10,
            refreshToken = "ref",
            scope = "prev-scope"
        )
        dataSource.save(withScope)

        val withoutScope = AuthToken(
            accessToken = "acc",
            tokenType = "Bearer",
            expiresIn = 20,
            refreshToken = null,
            scope = null // should remove key
        )

        // Act
        val saveResult = dataSource.save(withoutScope)

        // Assert
        assertTrue(saveResult.isSuccess())
        assertNull(dataStore.getTokenScope()) // removed
        assertEquals(20, dataStore.getTokenExpiresIn())
    }

    @Test
    fun clear_removesAllKeys() = runTest(testDispatcher) {
        // Arrange
        val token = AuthToken(
            accessToken = "acc",
            tokenType = "Bearer",
            expiresIn = 123,
            refreshToken = "ref",
            scope = "sc"
        )
        dataSource.save(token)

        // Act
        val clearResult = dataSource.clear()

        // Assert
        assertTrue(clearResult.isSuccess())
        assertNull(dataStore.getAccessToken())
        assertNull(dataStore.getRefreshToken())
        assertNull(dataStore.getTokenExpiresIn())
        assertNull(dataStore.getTokenScope())
        assertNull(dataStore.getTokenType())
    }

    @Test
    fun save_returnsFalseOnEncryptorException() = runTest(testDispatcher) {
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
        val saveResult = dataSource.save(token)

        // Assert
        assertFalse(saveResult.isSuccess())
    }
}
