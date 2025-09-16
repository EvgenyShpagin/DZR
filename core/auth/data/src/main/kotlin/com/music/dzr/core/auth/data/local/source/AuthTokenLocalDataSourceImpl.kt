package com.music.dzr.core.auth.data.local.source

import android.security.keystore.KeyPermanentlyInvalidatedException
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.music.dzr.core.auth.data.local.error.AuthStorageError
import com.music.dzr.core.auth.data.local.security.Encryptor
import com.music.dzr.core.auth.data.remote.dto.AuthToken
import com.music.dzr.core.result.Result
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.security.GeneralSecurityException
import javax.crypto.AEADBadTagException
import javax.crypto.BadPaddingException

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

    override suspend fun clearTokens(): Result<Unit, AuthStorageError> {
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

    private fun Throwable.toWriteError(): AuthStorageError = when (this) {
        is IOException, is IllegalStateException -> AuthStorageError.WriteFailed(this)
        else -> AuthStorageError.Unknown(this)
    }

    private fun Throwable.toCryptoError(): AuthStorageError = when (this) {
        is IllegalArgumentException -> AuthStorageError.DataCorrupted(this)
        is AEADBadTagException, is BadPaddingException -> AuthStorageError.IntegrityCheckFailed(this)
        is KeyPermanentlyInvalidatedException -> AuthStorageError.KeyInvalidated
        is GeneralSecurityException, is IllegalStateException -> AuthStorageError.CryptoFailure(this)
        else -> AuthStorageError.Unknown(this)
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
