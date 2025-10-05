package com.music.dzr.core.auth.domain.error

/**
 * A convenience property that indicates whether an [AuthError] requires
 * the user to start a new authorization flow from the beginning.
 */
val AuthError.requiresNewAuthorization: Boolean
    get() = when (this) {
        is AuthError.NotAuthenticated,
        is AuthError.InvalidGrant,
        is AuthError.SessionExpired,
        is AuthError.StateMismatch -> true

        else -> false
    }
