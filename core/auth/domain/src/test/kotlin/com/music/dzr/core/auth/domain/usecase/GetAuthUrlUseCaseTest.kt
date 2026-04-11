package com.music.dzr.core.auth.domain.usecase

import com.music.dzr.core.auth.domain.error.AuthError
import com.music.dzr.core.auth.domain.model.PermissionScope
import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
import com.music.dzr.core.result.Result
import com.music.dzr.core.testing.assertion.assertFailure
import com.music.dzr.core.testing.assertion.assertSuccess
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class GetAuthUrlUseCaseTest {

    private val repository: AuthTokenRepository = mockk()
    private val useCase = GetAuthUrlUseCase(repository)

    @Test
    fun returnsUrl_whenRepositorySucceeds() = runTest {
        // Arrange
        val scopes = listOf(PermissionScope.UserReadEmail)
        val url = "url"
        coEvery {
            repository.initiateAuthorization(scopes)
        } returns Result.Success(url)

        // Act
        val result = useCase(scopes)

        // Assert
        assertSuccess(result)
    }

    @Test
    fun propagatesFailure_whenRepositoryFails() = runTest {
        // Arrange
        val scopes = listOf(PermissionScope.UserReadEmail)
        val authError = AuthError.NotAuthenticated
        coEvery {
            repository.initiateAuthorization(scopes)
        } returns Result.Failure(authError)

        // Act
        val result = useCase(scopes)

        // Assert
        assertFailure(result)
    }
}
