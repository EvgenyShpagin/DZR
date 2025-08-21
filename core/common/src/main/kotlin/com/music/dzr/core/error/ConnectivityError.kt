package com.music.dzr.core.error

/**
 * Represents errors related to network connectivity.
 * These occur when the app cannot physically reach the server.
 */
sealed interface ConnectivityError : AppError {
    /** The device has no active internet connection. */
    data object NoInternet : ConnectivityError

    /** The network request timed out. */
    data object Timeout : ConnectivityError

    /**
     * A connection could not be established to the server host, despite an active internet connection.
     * This may be due to a DNS resolution failure or the host being down.
     */
    data object HostUnreachable : ConnectivityError
}