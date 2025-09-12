package com.music.dzr.core.auth.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

/**
 * In-memory Fake implementation of [DataStore]<[Preferences]> for auth token storage.
 *
 * Mirrors the contract of the real DataStore but keeps all state in memory.
 */
internal class FakeAuthTokenDataStore : DataStore<Preferences> {
    private val _data = MutableStateFlow(emptyPreferences())
    override val data: Flow<Preferences> = _data

    override suspend fun updateData(
        transform: suspend (t: Preferences) -> Preferences
    ): Preferences {
        val current = _data.value
        val updated = transform(current)
        _data.value = updated
        return updated
    }

    suspend fun getAccessToken() = data.first()[KEY_ACCESS]
    suspend fun getRefreshToken() = data.first()[KEY_REFRESH]
    suspend fun getTokenExpiresIn() = data.first()[KEY_EXPIRES]
    suspend fun getTokenScope() = data.first()[KEY_SCOPE]
    suspend fun getTokenType() = data.first()[KEY_TYPE]

    private companion object {
        private val KEY_ACCESS = stringPreferencesKey("access_token")
        private val KEY_REFRESH = stringPreferencesKey("refresh_token")
        private val KEY_EXPIRES = intPreferencesKey("expires_in")
        private val KEY_SCOPE = stringPreferencesKey("scope")
        private val KEY_TYPE = stringPreferencesKey("token_type")
    }
}
