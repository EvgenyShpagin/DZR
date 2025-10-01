package com.music.dzr.core.auth.data.local.model

import androidx.datastore.core.CorruptionException
import com.music.dzr.core.auth.data.local.security.FakeEncryptor
import kotlinx.coroutines.test.runTest
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTokenSerializerTest {
    private val fakeEncryptor = FakeEncryptor()
    private val authTokenSerializer = AuthTokenSerializer(fakeEncryptor)

    @Test
    fun defaultValue_whenNoData_returnsEmptyAuthToken() {
        // Arrange
        val expected = authToken { }

        // Act
        val actual = authTokenSerializer.defaultValue

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun writeTo_thenReadFrom_returnsSameAuthToken() = runTest {
        // Arrange
        val expected = authToken {
            accessToken = "access-123"
            refreshToken = "refresh-456" // optional present
            expiresIn = 3600
            scope = "user-read-private user-read-email" // optional present
            tokenType = "Bearer"
        }

        // Act
        val output = ByteArrayOutputStream()
        authTokenSerializer.writeTo(expected, output)
        val input = ByteArrayInputStream(output.toByteArray())
        val actual = authTokenSerializer.readFrom(input)

        // Assert
        assertEquals(expected, actual)
    }

    @Test(expected = CorruptionException::class)
    fun readFrom_withInvalidBytes_throwsCorruptionException() = runTest {
        // Arrange
        val invalidBytes = byteArrayOf(0)

        // Act
        authTokenSerializer.readFrom(ByteArrayInputStream(invalidBytes))
    }
}
