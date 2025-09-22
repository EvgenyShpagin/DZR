package com.music.dzr.core.auth.data.local.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.music.dzr.core.auth.data.local.security.Encryptor
import java.io.InputStream
import java.io.OutputStream

/**
 * A [Serializer] implementation for persisting [AuthSession] instances in DataStore.
 *
 * The [AuthSession] message is defined in
 * `core/auth/data/src/main/proto/auth_session.proto`.
 * It represents a short-lived snapshot of parameters required to complete the
 * OAuth 2.0 Authorization Code flow with PKCE after the browser/custom tab redirect.
 *
 * Persisting this model allows the app to recover from process death between
 * initiating the authorization request and handling the redirect callback.
 *
 * Properties of [AuthSession]:
 * - [AuthSession.codeVerifier]: The PKCE `code_verifier` generated for this
 *   authorization attempt (RFC 7636 §4.1). It is later sent to the token endpoint
 *   when exchanging the authorization code for tokens.
 * - [AuthSession.csrfState]: The OAuth 2.0 `state` value used for CSRF protection
 *   (RFC 6749 §10.12). It must match the `state` returned in the redirect.
 * - [AuthSession.createdAtMillis]: Epoch milliseconds when this session snapshot
 *   was created. Can be used to enforce an upper lifetime if needed.
 *
 * This serializer uses the provided [encryptor] to encrypt data before writing to disk
 * and decrypt it when reading. The encryption ensures sensitive OAuth parameters like
 * code verifiers and CSRF tokens remain protected at rest.
 */
internal class AuthSessionSerializer(
    private val encryptor: Encryptor
) : Serializer<AuthSession> {
    override val defaultValue: AuthSession = AuthSession.getDefaultInstance()

    // readFrom is already called on the data store background thread
    override suspend fun readFrom(input: InputStream): AuthSession {
        try {
            val encryptedInput = input.readBytes()
            val decryptedInput = encryptor.decrypt(encryptedInput)
            return AuthSession.parseFrom(decryptedInput)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    // writeTo is already called on the data store background thread
    override suspend fun writeTo(t: AuthSession, output: OutputStream) {
        val plainBytes = t.toByteArray()
        val encryptedBytes = encryptor.encrypt(plainBytes)
        output.write(encryptedBytes)
    }
}
