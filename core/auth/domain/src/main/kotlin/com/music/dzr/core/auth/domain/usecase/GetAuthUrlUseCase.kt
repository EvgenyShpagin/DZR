package com.music.dzr.core.auth.domain.usecase

import com.music.dzr.core.auth.domain.model.PermissionScope
import com.music.dzr.core.auth.domain.repository.AuthTokenRepository
import com.music.dzr.core.error.AppError
import com.music.dzr.core.result.Result

/**
 * Use case responsible for generating the authorization URL for the OAuth flow.
 *
 * This involves:
 * 1. Generating PKCE code verifier and challenge.
 * 2. Creating a CSRF state.
 * 3. Persisting the session (verifier + state).
 * 4. Building the URL with the correct parameters (client_id, redirect_uri, scopes, etc.).
 */
class GetAuthUrlUseCase(
    private val authTokenRepository: AuthTokenRepository
) {
    suspend operator fun invoke(scopes: List<PermissionScope>): Result<String, AppError> {
        return authTokenRepository.initiateAuthorization(scopes)
    }
}
