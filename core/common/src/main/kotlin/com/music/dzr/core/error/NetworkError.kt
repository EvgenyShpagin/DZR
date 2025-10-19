package com.music.dzr.core.error

/**
 * Represents errors reported by the remote server/API.
 * The server was reached, but it signaled a problem with the request or its own state.
 */
sealed interface NetworkError : AppError {
    /** User is not authenticated or the session has expired (e.g., HTTP 401). */
    data object Unauthorized : NetworkError

    /** User is authenticated but lacks permissions for the action (e.g., HTTP 403). */
    data object InsufficientPermissions : NetworkError

    /** The requested resource could not be found on the server (e.g., HTTP 404). */
    data object NotFound : NetworkError

    /** A server-side error occurred (e.g., HTTP 5xx). */
    data object ServerError : NetworkError

    /** An unexpected or unhandled server-side error. */
    data object Unexpected : NetworkError
}