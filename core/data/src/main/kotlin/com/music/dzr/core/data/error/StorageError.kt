package com.music.dzr.core.data.error

import com.music.dzr.core.error.AppError

/**
 * A hierarchy of errors produced by local data sources
 * (e.g., Preferences/DataStore, files, or databases).
 *
 * This is a data-layer specific error and should be handled or mapped
 * within the repository before being propagated to upper layers (domain/presentation).
 */
open class StorageError : AppError {
    /**
     * An optional cause of the error to provide more context for debugging.
     */
    open val cause: Throwable? = null

    /**
     * No record/value found in storage. This is a normal, expected state in many flows
     * (e.g., first launch before anything has been saved).
     */
    data object NotFound : StorageError()

    /**
     * Stored data is malformed or inconsistent (e.g., invalid format, missing required fields).
     */
    data class DataCorrupted(override val cause: Throwable?) : StorageError()

    /**
     * Reading from the underlying storage failed due to IO/state issues
     * (e.g., file access error, DataStore/DB read exception).
     */
    data class ReadFailed(override val cause: Throwable?) : StorageError()

    /**
     * Writing to the underlying storage failed due to IO/state issues
     * (e.g., disk full, permission error, DataStore/DB write exception).
     */
    data class WriteFailed(override val cause: Throwable?) : StorageError()

    /**
     * Fallback for unexpected cases. Inspect [cause] for details.
     */
    data class Unknown(override val cause: Throwable?) : StorageError()
}