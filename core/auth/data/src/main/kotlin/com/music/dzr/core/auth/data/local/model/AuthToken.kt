package com.music.dzr.core.auth.data.local.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.music.dzr.core.auth.data.local.security.Encryptor
import java.io.InputStream
import java.io.OutputStream

/**
 * A [Serializer] implementation for persisting [AuthToken] instances in DataStore.
 *
 * The [AuthToken] message is defined in
 * `core/auth/data/src/main/proto/auth_token.proto`.
 * It models the OAuth 2.0 token response that the application persists locally
 * to enable recovery across process death and to refresh access when needed.
 *
 * Properties of [AuthToken]:
 * - [AuthToken.accessToken]: Access token for API requests.
 * - [AuthToken.refreshToken]: Optional; may be absent in responses. Used to obtain new access tokens.
 * - [AuthToken.expiresIn]: Lifetime in seconds. Persist acquisition time alongside to compute expiry.
 * - [AuthToken.createdAtTimeMs]: Acquisition time (wall clock) in ms since Unix epoch.
 * - [AuthToken.scope]: Optional space-separated list of granted scopes; may be absent.
 * - [AuthToken.tokenType]: Token type, typically "Bearer".
 *
 * This serializer uses the provided [encryptor] to encrypt data before writing to disk
 * and decrypt it when reading. The encryption ensures sensitive OAuth tokens remain protected at rest.
 */
internal class AuthTokenSerializer(
    private val encryptor: Encryptor
) : Serializer<AuthToken> {
    override val defaultValue: AuthToken = AuthToken.getDefaultInstance()

    // readFrom is already called on the data store background thread
    override suspend fun readFrom(input: InputStream): AuthToken {
        try {
            val encryptedInput = input.readBytes()
            val decryptedInput = encryptor.decrypt(encryptedInput)
            return AuthToken.parseFrom(decryptedInput)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    // writeTo is already called on the data store background thread
    override suspend fun writeTo(t: AuthToken, output: OutputStream) {
        val plainBytes = t.toByteArray()
        val encryptedBytes = encryptor.encrypt(plainBytes)
        output.write(encryptedBytes)
    }
}