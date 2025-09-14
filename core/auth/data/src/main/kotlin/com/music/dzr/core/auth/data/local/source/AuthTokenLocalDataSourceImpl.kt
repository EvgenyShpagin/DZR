package com.music.dzr.core.auth.data.local.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.music.dzr.core.auth.data.remote.dto.AuthToken
import com.music.dzr.core.auth.data.security.Encryptor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Local storage for OAuth tokens backed by Preferences DataStore with AES/GCM encryption.
 * It stores fields from [AuthToken] as encrypted strings (except numeric values).
 * If [AuthToken.refreshToken] is null during save, the previously stored refresh token is preserved.
 */
internal class AuthTokenLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    private val encryptor: Encryptor
) : AuthTokenLocalDataSource {

    override suspend fun saveToken(token: AuthToken): Boolean {
        return try {
            val existingRefreshToken = dataStore.data
                .map { prefs -> prefs[Keys.REFRESH_TOKEN]?.let { encryptor.decrypt(it) } }
                .first()

            dataStore.edit { prefs ->
                prefs[Keys.ACCESS_TOKEN] = encryptor.encrypt(token.accessToken)
                // Preserve old refresh token if new one is null
                val refreshToPersist = token.refreshToken ?: existingRefreshToken
                if (refreshToPersist != null) {
                    prefs[Keys.REFRESH_TOKEN] = encryptor.encrypt(refreshToPersist)
                }
                prefs[Keys.EXPIRES_IN] = token.expiresIn
                token.scope?.let { scope ->
                    prefs[Keys.SCOPE] = encryptor.encrypt(scope)
                } ?: run {
                    prefs.remove(Keys.SCOPE)
                }
                prefs[Keys.TOKEN_TYPE] = encryptor.encrypt(token.tokenType)
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun clearTokens(): Boolean {
        return try {
            dataStore.edit { prefs ->
                prefs.clear()
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val EXPIRES_IN = intPreferencesKey("expires_in")
        val SCOPE = stringPreferencesKey("scope")
        val TOKEN_TYPE = stringPreferencesKey("token_type")
    }
}
