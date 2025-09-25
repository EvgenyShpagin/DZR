package com.music.dzr.core.auth.data.local.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.music.dzr.core.auth.data.local.error.toCryptoError
import com.music.dzr.core.auth.data.local.error.toReadError
import com.music.dzr.core.auth.data.local.error.toWriteError
import com.music.dzr.core.auth.data.local.security.Encryptor
import com.music.dzr.core.auth.data.remote.dto.AuthToken
import com.music.dzr.core.data.error.StorageError
import com.music.dzr.core.result.Result
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first

/**
 * Local storage for OAuth tokens backed by Preferences DataStore with AES/GCM encryption.
 * It stores fields from [AuthToken] as encrypted strings (except numeric values).
 * If [AuthToken.refreshToken] is null during save, the previously stored refresh token is preserved.
 */
internal class AuthTokenLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    private val encryptor: Encryptor
) : AuthTokenLocalDataSource {

    override suspend fun get(): Result<AuthToken, StorageError> {
        val encryptedToken = try {
            val prefs = dataStore.data.first()
            if (!prefs.contains(Keys.ACCESS_TOKEN)) {
                return Result.Failure(StorageError.NotFound)
            }
            AuthToken(
                accessToken = prefs[Keys.ACCESS_TOKEN]!!,
                refreshToken = prefs[Keys.REFRESH_TOKEN],
                expiresIn = prefs[Keys.EXPIRES_IN]!!,
                scope = prefs[Keys.SCOPE],
                tokenType = prefs[Keys.TOKEN_TYPE]!!
            )
        } catch (exception: Exception) {
            currentCoroutineContext().ensureActive()
            return Result.Failure(exception.toReadError())
        }

        val token = try {
            encryptedToken.decrypt()
        } catch (exception: Exception) {
            currentCoroutineContext().ensureActive()
            return Result.Failure(exception.toCryptoError())
        }
        return Result.Success(token)
    }

    override suspend fun save(token: AuthToken): Result<Unit, StorageError> {
        val encryptedToken = try {
            token.encrypt()
        } catch (exception: Exception) {
            currentCoroutineContext().ensureActive()
            return Result.Failure(exception.toCryptoError())
        }

        return try {
            dataStore.edit { prefs ->
                prefs[Keys.ACCESS_TOKEN] = encryptedToken.accessToken
                // Preserve old refresh token if new one is null (reuse encrypted blob)
                if (encryptedToken.refreshToken != null) {
                    prefs[Keys.REFRESH_TOKEN] = encryptedToken.refreshToken
                }
                prefs[Keys.EXPIRES_IN] = token.expiresIn
                if (encryptedToken.scope != null) {
                    prefs[Keys.SCOPE] = encryptedToken.scope
                } else {
                    prefs -= Keys.SCOPE
                }
                prefs[Keys.TOKEN_TYPE] = encryptedToken.tokenType
            }
            Result.Success(Unit)
        } catch (exception: Exception) {
            currentCoroutineContext().ensureActive()
            Result.Failure(exception.toWriteError())
        }
    }

    override suspend fun clear(): Result<Unit, StorageError> {
        return try {
            dataStore.edit { prefs ->
                prefs.clear()
            }
            Result.Success(Unit)
        } catch (exception: Exception) {
            currentCoroutineContext().ensureActive()
            Result.Failure(exception.toWriteError())
        }
    }

    private fun AuthToken.encrypt(): AuthToken = copy(
        accessToken = encryptor.encrypt(accessToken),
        tokenType = encryptor.encrypt(tokenType),
        scope = scope?.let { encryptor.encrypt(it) },
        refreshToken = refreshToken?.let { encryptor.encrypt(it) }
    )

    private fun AuthToken.decrypt(): AuthToken = copy(
        accessToken = encryptor.decrypt(accessToken),
        tokenType = encryptor.decrypt(tokenType),
        scope = scope?.let { encryptor.decrypt(it) },
        refreshToken = refreshToken?.let { encryptor.decrypt(it) }
    )

    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val EXPIRES_IN = intPreferencesKey("expires_in")
        val SCOPE = stringPreferencesKey("scope")
        val TOKEN_TYPE = stringPreferencesKey("token_type")
    }
}
