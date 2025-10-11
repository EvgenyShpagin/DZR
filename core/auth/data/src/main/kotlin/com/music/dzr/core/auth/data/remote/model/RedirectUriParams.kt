package com.music.dzr.core.auth.data.remote.model

/**
 * A sealed hierarchy representing the parsed result of an OAuth redirect URI.
 */
internal sealed interface RedirectUriParams {

    data object Invalid : RedirectUriParams

    data class Success(
        val code: String,
        val state: String,
    ) : RedirectUriParams

    data class Error(
        val error: String,
        val errorDescription: String?,
        val errorUri: String?,
        val state: String,
    ) : RedirectUriParams
}

internal fun RedirectUriParams.requireState(): String {
    return when (this) {
        is RedirectUriParams.Error -> state
        is RedirectUriParams.Success -> state
        RedirectUriParams.Invalid -> throw IllegalArgumentException("Invalid redirect URI")
    }
}
