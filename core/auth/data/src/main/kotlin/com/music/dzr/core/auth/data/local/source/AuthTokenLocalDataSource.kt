package com.music.dzr.core.auth.data.local.source

import com.music.dzr.core.auth.data.remote.dto.AuthToken

/**
 * Abstraction for persisting OAuth token payload locally.
 */
internal interface AuthTokenLocalDataSource {
    /**
     * Persist the provided token payload.
     *
     * @param token Network DTO with access token information to be stored.
     * @return `true` if the token was saved successfully, `false` otherwise.
     */
    suspend fun saveToken(token: AuthToken): Boolean

    /**
     * Remove any stored token-related data from the local storage.
     *
     * @return `true` if data was cleared successfully, `false` otherwise.
     */
    suspend fun clearTokens(): Boolean
}
