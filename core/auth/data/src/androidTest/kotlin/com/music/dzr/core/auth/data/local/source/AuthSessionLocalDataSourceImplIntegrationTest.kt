package com.music.dzr.core.auth.data.local.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.filters.MediumTest
import com.music.dzr.core.auth.data.local.error.SecureStorageError
import com.music.dzr.core.auth.data.local.model.AuthSession
import com.music.dzr.core.auth.data.local.model.AuthSessionSerializer
import com.music.dzr.core.auth.data.local.model.authSession
import com.music.dzr.core.auth.data.local.security.KeystoreEncryptor
import com.music.dzr.core.result.isFailure
import com.music.dzr.core.result.isSuccess
import com.music.dzr.core.storage.error.StorageError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import java.io.File
import java.io.RandomAccessFile
import java.security.KeyStore
import java.util.UUID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Android instrumentation integration test for [AuthSessionLocalDataSource] with a real, file-backed
 * [DataStore]<[AuthSession]> using [AuthSessionSerializer] and [KeystoreEncryptor].
 */
@MediumTest
class AuthSessionLocalDataSourceImplIntegrationTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var dataSource: AuthSessionLocalDataSource
    private lateinit var dataStore: DataStore<AuthSession>
    private lateinit var scope: CoroutineScope
    private lateinit var scopeJob: Job
    private lateinit var dataStoreFile: File
    private lateinit var encryptor: KeystoreEncryptor
    private lateinit var serializer: AuthSessionSerializer
    private lateinit var alias: String

    @BeforeTest
    fun setUp() {
        // Use app-specific storage to avoid flaky permissions and ensure isolation per test run
        val context = getApplicationContext<Context>()
        initSerializerWithEncryptorAlias()
        initScope()
        dataStoreFile = context.dataStoreFile("auth_session_${UUID.randomUUID()}.pb")
        initDataSource()
    }

    @AfterTest
    fun tearDown() = runTest {
        // Ensure all scheduled tasks complete
        cancelScope()
        dataStoreFile.delete()
        clearAlias()
    }


    @Test
    fun save_whenValid_returnsSuccess() = runTest(dispatcher) {
        // Arrange
        val session = authSession {
            codeVerifier = "verifier-123"
            csrfState = "state-abc"
            createdAtMillis = 123L
        }

        // Act
        val result = dataSource.save(session)

        // Assert
        assertTrue { result.isSuccess() }
    }

    @Test
    fun save_overwritesExistingSession() = runTest(dispatcher) {
        // Arrange
        val initialSession = authSession {
            codeVerifier = "initial-verifier"
            csrfState = "initial-state"
        }
        dataSource.save(initialSession)

        val updatedSession = authSession {
            codeVerifier = "updated-verifier"
            csrfState = "updated-state"
        }

        // Act
        dataSource.save(updatedSession)

        // Assert
        val result = dataSource.get()
        assertTrue(result.isSuccess())
        assertEquals(updatedSession, result.data)
    }

    @Test
    fun get_afterSave_returnsSavedAuthSession() = runTest(dispatcher) {
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
    fun get_whenNoData_returnsNotFoundError() = runTest(dispatcher) {
        // Arrange: No data is saved

        // Act
        val result = dataSource.get()

        // Assert
        assertTrue(result.isFailure())
        assertIs<StorageError.NotFound>(result.error)
    }

    @Test
    fun get_afterClear_returnsNotFoundError() = runTest(dispatcher) {
        // Arrange
        val session = authSession {
            codeVerifier = "verifier-abc"
            csrfState = "state-123"
        }
        dataSource.save(session)
        dataSource.clear()

        // Act
        val result = dataSource.get()

        // Assert
        assertTrue(result.isFailure())
        assertIs<StorageError.NotFound>(result.error)
    }

    @Test
    fun get_whenDecryptFails_withDifferentKeystoreAlias_returnsSecureStorageDecryptionFailed() =
        runTest(dispatcher) {
            // Arrange: save with current alias
            dataSource.save(authSession { codeVerifier = "secure" })
            // Recreate DataStore using a different keystore alias so decrypt fails
            recreateDataStoreWithDifferentAlias()

            // Act
            val result = dataSource.get()

            // Assert
            assertTrue(result.isFailure())
            assertIs<SecureStorageError.DecryptionFailed>(result.error)
        }

    @Test
    fun get_whenCiphertextTampered_returnsSecureStorageDecryptionFailed() = runTest(dispatcher) {
        // Arrange: save and ensure bytes are flushed
        dataSource.save(authSession { codeVerifier = "secure" })
        cancelScope()
        tamperDataStoreFile()
        // Recreate DataStore with the same alias
        initScope()
        initDataSource()

        // Act
        val result = dataSource.get()

        // Assert
        assertTrue(result.isFailure())
        assertIs<SecureStorageError.DecryptionFailed>(result.error)
    }

    @Test
    fun clear_whenDataExists_returnsSuccess() = runTest(dispatcher) {
        // Arrange
        val session = authSession {
            codeVerifier = "verifier-to-clear"
            csrfState = "state-to-clear"
        }
        dataSource.save(session)

        // Act
        val result = dataSource.clear()

        // Assert
        assertTrue(result.isSuccess())
    }

    @Test
    fun clear_whenNoData_returnsSuccess() = runTest(dispatcher) {
        // Arrange: No data saved

        // Act
        val result = dataSource.clear()

        // Assert
        assertTrue(result.isSuccess())
    }

    private fun tamperDataStoreFile() {
        if (!dataStoreFile.exists() || dataStoreFile.length() == 0L) return
        val file = RandomAccessFile(dataStoreFile, "rw")
        try {
            val len = file.length()
            file.seek(len - 1)
            val b = file.read()
            file.seek(len - 1)
            file.write(b xor 0x01)
        } finally {
            file.close()
        }
    }

    private fun clearAlias() {
        val keystore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        keystore.deleteEntry(alias)
    }

    private suspend fun recreateDataStoreWithDifferentAlias() {
        cancelScope()
        clearAlias()
        alias = "auth_session_it_other_${UUID.randomUUID()}"
        encryptor = KeystoreEncryptor(alias)
        serializer = AuthSessionSerializer(encryptor)
        initScope()
        initDataSource()
    }

    private fun initSerializerWithEncryptorAlias(
        alias: String = "auth_session_it_${UUID.randomUUID()}"
    ) {
        this.alias = alias
        encryptor = KeystoreEncryptor(alias)
        serializer = AuthSessionSerializer(encryptor)
    }

    private suspend fun cancelScope() {
        // Close current DataStore and swap encryptor/serializer
        dispatcher.scheduler.advanceUntilIdle()
        scopeJob.cancel()
        // Allow cancellation to propagate and resources to be released
        dispatcher.scheduler.advanceUntilIdle()
        // Ensure the previous scope completes before proceeding
        scopeJob.join()
    }

    private fun initScope() {
        scopeJob = SupervisorJob()
        scope = CoroutineScope(scopeJob + dispatcher)
    }

    private fun initDataSource() {
        dataStore = DataStoreFactory.create(
            serializer = serializer,
            scope = scope,
            produceFile = { dataStoreFile }
        )
        dataSource = AuthSessionLocalDataSourceImpl(dataStore)
    }

}
