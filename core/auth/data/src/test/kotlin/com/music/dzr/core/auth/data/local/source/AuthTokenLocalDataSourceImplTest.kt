package com.music.dzr.core.auth.data.local.source

import com.music.dzr.core.auth.data.local.error.SecureStorageError
import com.music.dzr.core.auth.data.local.model.AuthToken
import com.music.dzr.core.auth.data.local.model.authToken
import com.music.dzr.core.auth.data.local.security.EncryptorException
import com.music.dzr.core.result.isFailure
import com.music.dzr.core.result.isSuccess
import com.music.dzr.core.storage.error.StorageError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class AuthTokenLocalDataSourceImplTest {

    private lateinit var dataStore: TestAuthTokenDataStore
    private lateinit var dataSource: AuthTokenLocalDataSource

    @BeforeTest
    fun setUp() {
        dataStore = TestAuthTokenDataStore(AuthToken.getDefaultInstance())
        dataSource = AuthTokenLocalDataSourceImpl(dataStore)
    }

    @Test
    fun save_whenValid_returnsSuccess() = runTest {
        // Arrange
        val token = authToken {
            accessToken = "access-token-123"
            refreshToken = "refresh-token-456"
            expiresIn = 3600
            scope = "read write"
            tokenType = "Bearer"
        }

        // Act
        val result = dataSource.save(token)

        // Assert
        assertTrue(result.isSuccess())
    }

    @Test
    fun save_whenUpdateDataThrowsIOException_returnsWriteFailed() = runTest {
        // Arrange
        dataStore.exceptionToThrow = IOException()

        // Act
        val result = dataSource.save(authToken { accessToken = "x" })

        // Assert
        assertTrue(result.isFailure())
        assertIs<StorageError.WriteFailed>(result.error)
    }

    @Test
    fun save_onUnexpectedException_returnsUnknown() = runTest {
        // Arrange
        dataStore.exceptionToThrow = RuntimeException("boom")

        // Act
        val result = dataSource.save(authToken { accessToken = "x" })

        // Assert
        assertTrue(result.isFailure())
        assertIs<StorageError.Unknown>(result.error)
    }

    @Test
    fun save_onInitializationException_returnsSecureStorageInitializationFailed() = runTest {
        // Arrange
        dataStore.exceptionToThrow = EncryptorException.Initialization(IllegalStateException())

        // Act
        val result = dataSource.save(authToken { accessToken = "x" })

        // Assert
        assertTrue(result.isFailure())
        assertIs<SecureStorageError.InitializationFailed>(result.error)
    }

    @Test
    fun save_onEncryptionException_returnsSecureStorageEncryptionFailed() = runTest {
        // Arrange
        dataStore.exceptionToThrow = EncryptorException.Encryption(IllegalStateException())

        // Act
        val result = dataSource.save(authToken { accessToken = "x" })

        // Assert
        assertTrue(result.isFailure())
        assertIs<SecureStorageError.EncryptionFailed>(result.error)
    }

    @Test
    fun save_whenCancelled_propagatesCancellation() = runTest {
        // Arrange
        dataStore.exceptionToThrow = CancellationException()

        // Act
        val result = runCatching { dataSource.save(authToken { accessToken = "x" }) }

        // Assert
        assertIs<CancellationException>(result.exceptionOrNull())
    }

    @Test
    fun get_afterSave_returnsSavedAuthToken() = runTest {
        // Arrange
        val expected = authToken {
            accessToken = "access-token-abc"
            refreshToken = "refresh-token-def"
            expiresIn = 1800
            scope = "user-read"
            tokenType = "Bearer"
        }
        dataSource.save(expected)

        // Act
        val result = dataSource.get()

        // Assert
        assertTrue(result.isSuccess())
        assertEquals(expected, result.data)
    }

    @Test
    fun get_whenNoData_returnsNotFound() = runTest {
        // Arrange: DataStore holds default instance

        // Act
        val result = dataSource.get()

        // Assert
        assertTrue(result.isFailure())
        assertIs<StorageError.NotFound>(result.error)
    }

    @Test
    fun get_onIOException_returnsReadFailed() = runTest {
        // Arrange
        dataStore.exceptionToThrow = IOException("io")

        // Act
        val result = dataSource.get()

        // Assert
        assertTrue(result.isFailure())
        assertIs<StorageError.ReadFailed>(result.error)
    }

    @Test
    fun get_onInitializationException_returnsSecureStorageInitializationFailed() = runTest {
        // Arrange
        dataStore.exceptionToThrow =
            EncryptorException.Initialization(IllegalStateException())

        // Act
        val result = dataSource.get()

        // Assert
        assertTrue(result.isFailure())
        assertIs<SecureStorageError.InitializationFailed>(result.error)
    }

    @Test
    fun get_onDecryptionException_returnsSecureStorageDecryptionFailed() = runTest {
        // Arrange
        dataStore.exceptionToThrow = EncryptorException.Decryption(IllegalStateException())

        // Act
        val result = dataSource.get()

        // Assert
        assertTrue(result.isFailure())
        assertIs<SecureStorageError.DecryptionFailed>(result.error)
    }

    @Test
    fun get_onUnexpectedException_returnsUnknown() = runTest {
        // Arrange
        dataStore.exceptionToThrow = RuntimeException("boom")

        // Act
        val result = dataSource.get()

        // Assert
        assertTrue(result.isFailure())
        assertIs<StorageError.Unknown>(result.error)
    }

    @Test
    fun get_whenCancelled_propagatesCancellation() = runTest {
        // Arrange
        dataStore.exceptionToThrow = CancellationException()

        // Act
        val result = runCatching { dataSource.get() }

        // Assert
        assertIs<CancellationException>(result.exceptionOrNull())
    }

    @Test
    fun clear_whenDataExists_returnsSuccess_andSubsequentGetReturnsNotFound() = runTest {
        // Arrange
        val toSave = authToken {
            accessToken = "to-clear"
        }
        dataSource.save(toSave)

        // Act
        val clearResult = dataSource.clear()
        val getResult = dataSource.get()

        // Assert
        assertTrue(clearResult.isSuccess())
        assertTrue(getResult.isFailure())
        assertIs<StorageError.NotFound>(getResult.error)
    }

    @Test
    fun clear_onIOException_returnsWriteFailed() = runTest {
        // Arrange
        dataStore.exceptionToThrow = IOException()

        // Act
        val result = dataSource.clear()

        // Assert
        assertTrue(result.isFailure())
        assertIs<StorageError.WriteFailed>(result.error)
    }

    @Test
    fun clear_onUnexpectedException_returnsUnknown() = runTest {
        // Arrange
        dataStore.exceptionToThrow = RuntimeException("boom")

        // Act
        val result = dataSource.clear()

        // Assert
        assertTrue(result.isFailure())
        assertIs<StorageError.Unknown>(result.error)
    }

    @Test
    fun clear_whenCancelled_propagatesCancellation() = runTest {
        // Arrange
        dataStore.exceptionToThrow = CancellationException()

        // Act
        val result = runCatching { dataSource.clear() }

        // Assert
        assertIs<CancellationException>(result.exceptionOrNull())
    }
}
