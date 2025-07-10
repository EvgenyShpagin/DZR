package com.music.dzr.core.result

/**
 * Represents the result of an operation that can either succeed or fail.
 *
 * ## Usage
 *
 * ### Returning a result from a function:
 * ```kotlin
 * suspend fun loadUser(id: String): Result<User, UserError> {
 *     return try {
 *         val user = userRepository.getUser(id)
 *         Result.Success(user)
 *     } catch (e: Exception) {
 *         Result.Failure(UserError.NotFound(id))
 *     }
 * }
 * ```
 *
 * ### Handling the result:
 * ```kotlin
 * when (val result = loadUser("123")) {
 *     is Result.Success -> println("User loaded: ${result.data.name}")
 *     is Result.Failure -> when (result.error) { /* handle specific */}
 * }
 * ```
 *
 * ## Benefits
 *
 * - **Type safety**: The compiler ensures that all possible errors are handled
 * - **Explicitness**: Function signatures clearly show what errors can occur
 * - **Composition**: Easy to combine results of operations
 * - **Exception-free**: Avoids hidden exceptions and their unexpected propagation
 *
 * @param D the type of data returned when the operation succeeds
 * @param E the type of error that extends [AppError], which can occur on failure
 *
 * @see AppError
 */
sealed class Result<out D, out E : AppError> {
    /**
     * Represents a successful operation result.
     *
     * Contains the data obtained as a result of successful operation execution.
     */
    data class Success<out D>(val data: D) : Result<D, Nothing>()
    /**
     * Represents a failed operation result.
     *
     * Contains information about the error that occurred during operation execution.
     */
    data class Failure<out E : AppError>(val error: E) : Result<Nothing, E>()
}