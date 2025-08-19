package com.music.dzr.core.error

/**
 * Represents errors related to network connectivity.
 * These occur when the app cannot physically reach the server.
 */
sealed interface ConnectivityError : AppError {
    /** The device has no internet connection or the server is unreachable. */
    data object Offline : ConnectivityError

    /** The network request timed out. */
    data object Timeout : ConnectivityError

    /** A connection could not be established to the server. */
    data object ServerUnreachable : ConnectivityError
}