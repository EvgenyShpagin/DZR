package com.music.dzr.core.auth.data.remote.oauth

import com.music.dzr.core.auth.data.remote.model.RedirectUriParams
import java.net.URI
import java.net.URISyntaxException
import java.net.URLDecoder

/**
 * Parses the full redirect URI string from
 * an OAuth flow into a structured [RedirectUriParams] object.
 */
internal fun String.parseRedirectUriParams(): RedirectUriParams {
    val uri = try {
        URI(this)
    } catch (_: URISyntaxException) {
        return RedirectUriParams.Invalid
    }

    val params = uri.rawQuery?.let { parseQueryParams(it) } ?: emptyMap()

    return tryParseSuccess(params)
        ?: tryParseError(params)
        ?: RedirectUriParams.Invalid
}

/**
 * Attempts to parse the parameters as a successful authorization response.
 * Requires `code` and `state` query parameters to be present.
 */
private fun tryParseSuccess(params: Map<String, String>): RedirectUriParams.Success? {
    val code = params["code"] ?: return null
    val state = params["state"] ?: return null
    return RedirectUriParams.Success(code = code, state = state)
}

/**
 * Attempts to parse the parameters as an authorization error response.
 * Requires `error` and `state` query parameters to be present.
 */
private fun tryParseError(params: Map<String, String>): RedirectUriParams.Error? {
    return RedirectUriParams.Error(
        error = params["error"] ?: return null,
        state = params["state"] ?: return null,
        errorDescription = params["error_description"],
        errorUri = params["error_uri"]
    )
}

/**
 * Decodes a raw query string into a map of the first occurrences of keys to values.
 * Uses `URLDecoder` with UTF-8, treating '+' as a space, per application/x-www-form-urlencoded semantics.
 */
private fun parseQueryParams(rawQuery: String): Map<String, String> {
    if (rawQuery.isEmpty()) return emptyMap()

    return buildMap {
        rawQuery.split('&').forEach { pair ->
            if (pair.isBlank()) return@forEach
            val parts = pair.split('=', limit = 2)
            val name = decodeComponent(parts[0])
            val value = if (parts.count() > 1) decodeComponent(parts[1]) else ""
            putIfAbsent(name, value)
        }
    }
}

private fun decodeComponent(s: String): String = URLDecoder.decode(s, "UTF-8")
