package com.music.dzr.core.auth.data.local.source

import androidx.datastore.core.DataStore
import com.music.dzr.core.auth.data.local.model.AuthToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.updateAndGet

/**
 * Exception configurable in-memory test implementation of [DataStore]<[AuthToken]>.
 *
 * Mirrors the contract of the real DataStore but keeps all state in memory.
 */
internal class TestAuthTokenDataStore(initialValue: AuthToken) : DataStore<AuthToken> {
    private val state = MutableStateFlow(initialValue)

    /**
     * Exception to throw when reading and updating data.
     */
    var exceptionToThrow: Exception? = null

    override val data get() = exceptionToThrow?.let { flow { throw it } } ?: state

    override suspend fun updateData(
        transform: suspend (AuthToken) -> AuthToken
    ): AuthToken {
        exceptionToThrow?.let { throw it }
        return state.updateAndGet { transform(it) }
    }
}
