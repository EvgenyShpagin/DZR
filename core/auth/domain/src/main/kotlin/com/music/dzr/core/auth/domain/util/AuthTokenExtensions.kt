package com.music.dzr.core.auth.domain.util

import com.music.dzr.core.auth.domain.model.AuthToken

/**
 * Checks if the access token has expired.
 *
 * @param currentTimeMillis The current time in milliseconds to check against.
 *                          Defaults to System.currentTimeMillis().
 * @param safetyBufferSeconds A safety buffer in seconds to treat the token as expired
 *                            a bit earlier to account for network latency.
 * @return `true` if the token is expired, `false` otherwise.
 */
fun AuthToken.isExpired(
    currentTimeMillis: Long = System.currentTimeMillis(),
    safetyBufferSeconds: Int = 30
): Boolean {
    val safetyBufferMillis = safetyBufferSeconds * 1000L
    return currentTimeMillis >= (expiresAtMillis - safetyBufferMillis)
}
