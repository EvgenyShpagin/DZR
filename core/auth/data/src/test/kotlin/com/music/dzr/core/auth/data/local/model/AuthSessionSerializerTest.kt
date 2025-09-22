package com.music.dzr.core.auth.data.local.model

import androidx.datastore.core.CorruptionException
import com.music.dzr.core.auth.data.local.security.FakeEncryptor
import kotlinx.coroutines.test.runTest
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthSessionSerializerTest {
    private val fakeEncryptor = FakeEncryptor()
    private val authSessionSerializer = AuthSessionSerializer(fakeEncryptor)

    @Test
    fun defaultValue_whenNoData_returnsEmptyAuthSession() {
        // Arrange
        val expected = authSession { }

        // Act
        val actual = authSessionSerializer.defaultValue

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun writeTo_thenReadFrom_returnsSameAuthSession() = runTest {
        // Arrange
        val expected = authSession {
            this.codeVerifier = "QERTYUIOP{}"
            this.csrfState = "ASDFGHJKL:"
            this.createdAtMillis = 123123
        }

        // Act
        val output = ByteArrayOutputStream()
        authSessionSerializer.writeTo(expected, output)
        val input = ByteArrayInputStream(output.toByteArray())
        val actual = authSessionSerializer.readFrom(input)

        // Assert
        assertEquals(expected, actual)
    }

    @Test(expected = CorruptionException::class)
    fun readFrom_withInvalidBytes_throwsCorruptionException() = runTest {
        // Arrange
        val invalidBytes = byteArrayOf(0)

        // Act
        authSessionSerializer.readFrom(ByteArrayInputStream(invalidBytes))
    }
}