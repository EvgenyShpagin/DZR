package com.music.dzr.core.auth.data.local.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.filters.MediumTest
import com.music.dzr.core.auth.data.local.error.SecureStorageError
import com.music.dzr.core.auth.data.local.model.AuthToken
import com.music.dzr.core.auth.data.local.model.AuthTokenSerializer
import com.music.dzr.core.auth.data.local.model.authToken
import com.music.dzr.core.auth.data.local.security.KeystoreEncryptor
import com.music.dzr.core.data.error.StorageError
import com.music.dzr.core.result.isFailure
import com.music.dzr.core.result.isSuccess
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
 * Android instrumentation integration test for [AuthTokenLocalDataSource] with a real, file-backed
 * [DataStore]<[AuthToken]> using [AuthTokenSerializer] and [KeystoreEncryptor].
 */
@MediumTest
class AuthTokenLocalDataSourceImplIntegrationTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var dataSource: AuthTokenLocalDataSource
    private lateinit var dataStore: DataStore<AuthToken>
    private lateinit var scope: CoroutineScope
    private lateinit var scopeJob: Job
    private lateinit var dataStoreFile: File
    private lateinit var encryptor: KeystoreEncryptor
    private lateinit var serializer: AuthTokenSerializer
    private lateinit var alias: String

    @BeforeTest
    fun setUp() {
        // Use app-specific storage to avoid flaky permissions and ensure isolation per test run
        val context = getApplicationContext<Context>()
        initSerializerWithEncryptorAlias()
        initScope()
        dataStoreFile = context.dataStoreFile("auth_token_${UUID.randomUUID()}.pb")
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
        val token = authToken {
            accessToken = "access-token-123"
            refreshToken = "refresh-token-456"
            expiresIn = 3600
        }

        // Act
        val result = dataSource.save(token)

        // Assert
        assertTrue { result.isSuccess() }
    }

    @Test
    fun save_overwritesExistingToken() = runTest(dispatcher) {
        // Arrange
        val initialToken = authToken { accessToken = "initial-token" }
        dataSource.save(initialToken)

        val updatedToken = authToken { accessToken = "updated-token" }

        // Act
        dataSource.save(updatedToken)

        // Assert
        val result = dataSource.get()
        assertTrue(result.isSuccess())
        assertEquals(updatedToken, result.data)
    }

    @Test
    fun get_afterSave_returnsSavedAuthToken() = runTest(dispatcher) {
        // Arrange
        val expected = authToken {
            accessToken = "access-token-xyz"
            refreshToken = "refresh-token-abc"
            expiresIn = 1800
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
        val token = authToken { accessToken = "token-abc" }
        dataSource.save(token)
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
            dataSource.save(authToken { accessToken = "secure" })
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
        dataSource.save(authToken { accessToken = "secure" })
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
        val token = authToken { accessToken = "token-to-clear" }
        dataSource.save(token)

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
        alias = "auth_token_it_other_${UUID.randomUUID()}"
        encryptor = KeystoreEncryptor(alias)
        serializer = AuthTokenSerializer(encryptor)
        initScope()
        initDataSource()
    }

    private fun initSerializerWithEncryptorAlias(
        alias: String = "auth_token_it_${UUID.randomUUID()}"
    ) {
        this.alias = alias
        encryptor = KeystoreEncryptor(alias)
        serializer = AuthTokenSerializer(encryptor)
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
        dataSource = AuthTokenLocalDataSourceImpl(dataStore)
    }
}
