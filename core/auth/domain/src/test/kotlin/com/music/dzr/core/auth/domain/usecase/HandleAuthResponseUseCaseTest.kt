package com.music.dzr.core.auth.domain.usecase

import com.music.dzr.core.auth.domain.error.AuthError
import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
import com.music.dzr.core.result.Result
import com.music.dzr.core.testing.assertion.assertFailure
import com.music.dzr.core.testing.assertion.assertSuccess
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class HandleAuthResponseUseCaseTest {

    val repository: AuthTokenRepository = mockk()
    val useCase = HandleAuthResponseUseCase(repository)

    @Test
    fun returnsUrl_whenRepositorySucceeds() = runTest {
        // Arrange
        val responseUri = "uri"
        coEvery {
            repository.completeAuthorization(responseUri)
        } returns Result.Success(Unit)

        // Act
        val result = useCase(responseUri)

        // Assert
        assertSuccess(result)
    }

    @Test
    fun propagatesFailure_whenRepositoryFails() = runTest {
        // Arrange
        val responseUri = "uri"
        coEvery {
            repository.completeAuthorization(responseUri)
        } returns Result.Failure(AuthError.InvalidClient)

        // Act
        val result = useCase(responseUri)

        // Assert
        assertFailure(result)
    }
}