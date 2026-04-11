package com.music.dzr.core.auth.domain.usecase

import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
import com.music.dzr.core.error.AppError
import com.music.dzr.core.result.Result

/**
 * Use case responsible for handling the redirect URI from the authorization server.
 *
 * It delegates the validation of the state and the exchange of the code for tokens
 * to the repository.
 */
class HandleAuthResponseUseCase(
    private val authTokenRepository: AuthTokenRepository
) {
    suspend operator fun invoke(responseUri: String): Result<Unit, AppError> {
        return authTokenRepository.completeAuthorization(responseUri)
    }
}
