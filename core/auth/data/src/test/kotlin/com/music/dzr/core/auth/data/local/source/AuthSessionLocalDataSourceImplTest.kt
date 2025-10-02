package com.music.dzr.core.auth.data.local.source

import com.music.dzr.core.auth.data.local.error.SecureStorageError
import com.music.dzr.core.auth.data.local.model.AuthSession
import com.music.dzr.core.auth.data.local.model.authSession
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

class AuthSessionLocalDataSourceImplTest {

    private lateinit var dataStore: TestAuthSessionDataStore
    private lateinit var dataSource: AuthSessionLocalDataSource

    @BeforeTest
    fun setUp() {
        dataStore = TestAuthSessionDataStore(AuthSession.getDefaultInstance())
        dataSource = AuthSessionLocalDataSourceImpl(dataStore)
    }

    @Test
    fun save_whenValid_returnsSuccess() = runTest {
        // Arrange
        val session = authSession {
            codeVerifier = "verifier-123"
            csrfState = "state-abc"
            createdAtMillis = 123L
        }

        // Act
        val result = dataSource.save(session)

        // Assert
        assertTrue(result.isSuccess())
    }

    @Test
    fun save_whenUpdateDataThrowsIOException_returnsWriteFailed() = runTest {
        // Arrange
        dataStore.exceptionToThrow = IOException()
        // Act
        val result = dataSource.save(authSession { codeVerifier = "x" })

        // Assert
        assertTrue(result.isFailure())
        assertIs<StorageError.WriteFailed>(result.error)
    }

    @Test
    fun save_onUnexpectedException_returnsUnknown() = runTest {
        // Arrange
        dataStore.exceptionToThrow = RuntimeException("boom")

        // Act
        val result = dataSource.save(authSession { codeVerifier = "x" })

        // Assert
        assertTrue(result.isFailure())
        assertIs<StorageError.Unknown>(result.error)
    }

    @Test
    fun save_onInitializationException_returnsSecureStorageInitializationFailed() = runTest {
        // Arrange
        dataStore.exceptionToThrow = EncryptorException.Initialization(IllegalStateException())

        // Act
        val result = dataSource.save(authSession { codeVerifier = "x" })

        // Assert
        assertTrue(result.isFailure())
        assertIs<SecureStorageError.InitializationFailed>(result.error)
    }

    @Test
    fun save_onEncryptionException_returnsSecureStorageEncryptionFailed() = runTest {
        // Arrange
        dataStore.exceptionToThrow = EncryptorException.Encryption(IllegalStateException())

        // Act
        val result = dataSource.save(authSession { codeVerifier = "x" })

        // Assert
        assertTrue(result.isFailure())
        assertIs<SecureStorageError.EncryptionFailed>(result.error)
    }

    @Test
    fun save_whenCancelled_propagatesCancellation() = runTest {
        // Arrange
        dataStore.exceptionToThrow = CancellationException()

        // Act
        val result = runCatching { dataSource.save(authSession { codeVerifier = "x" }) }

        // Assert
        assertIs<CancellationException>(result.exceptionOrNull())
    }

    @Test
    fun get_afterSave_returnsSavedAuthSession() = runTest {
        // Arrange
        val expected = authSession {
            codeVerifier = "verifier-xyz"
            csrfState = "state-789"
            createdAtMillis = 987654321L
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
        val toSave = authSession {
            codeVerifier = "to-clear"
            csrfState = "to-clear"
            createdAtMillis = 42L
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