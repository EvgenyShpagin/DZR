package com.music.dzr.core.auth.data.remote.oauth

import android.net.Uri
import androidx.core.net.toUri
import com.music.dzr.core.auth.data.remote.model.RedirectUriParams

/**
 * Parses the full redirect URI string from
 * an OAuth flow into a structured [RedirectUriParams] object.
 */
internal fun String.parseRedirectUriParams(): RedirectUriParams {
    val uri = toUri()
    return uri.tryParseSuccess()
        ?: uri.tryParseError()
        ?: RedirectUriParams.Invalid
}

/**
 * Attempts to parse the URI as a successful authorization response.
 * Requires `code` and `state` query parameters to be present.
 */
private fun Uri.tryParseSuccess(): RedirectUriParams.Success? {
    return RedirectUriParams.Success(
        code = getQueryParameter("code") ?: return null,
        state = getQueryParameter("state") ?: return null
    )
}

/**
 * Attempts to parse the URI as an authorization error response.
 * Requires `error` and `state` query parameters to be present.
 */
private fun Uri.tryParseError(): RedirectUriParams.Error? {
    return RedirectUriParams.Error(
        error = getQueryParameter("error") ?: return null,
        errorDescription = getQueryParameter("error_description"),
        errorUri = getQueryParameter("error_uri"),
        state = getQueryParameter("state") ?: return null
    )
}
