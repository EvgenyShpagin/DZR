package com.music.dzr.core.error

/**
 * Represents errors that can occur during persistence operations,
 * such as reading from or writing to a local database, file storage, and so on.
 */
sealed interface PersistenceError : AppError {
    /**
     * Indicates that a write operation failed because there is no available space on the device.
     * This is a user-recoverable error; the UI can prompt the user to free up storage.
     */
    data object NoAvailableSpace : PersistenceError

    /**
     * Represents an unexpected and unrecoverable error in the persistence layer.
     * This can be caused by I/O failures, data corruption, or other system-level issues.
     * Such errors should be logged for analysis.
     */
    data object Unexpected : PersistenceError
}
