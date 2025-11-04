@file:OptIn(ExperimentalContracts::class)

package com.music.dzr.core.testing.assertion

import com.music.dzr.core.error.AppError
import com.music.dzr.core.result.Result
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.test.assertEquals
import kotlin.test.assertIs


/**
 * Asserts that [actual] is [Result.Success].
 */
fun <D, E : AppError> assertSuccess(actual: Result<D, E>, message: String? = null) {
    contract { returns() implies (actual is Result.Success<D>) }
    assertIs<Result.Success<D>>(actual, message ?: "Expected value to be Result.Success.")
}

/**
 * Asserts that [actual] is [Result.Success] with data equal to [expectedData].
 */
fun <D, E : AppError> assertSuccessEquals(
    expectedData: D,
    actual: Result<D, E>,
    message: String? = null
) {
    contract { returns() implies (actual is Result.Success<D>) }
    assertSuccess(actual, message)
    assertEquals(
        expected = expectedData,
        actual = actual.data,
        message = message ?: "Expected data to be $expectedData, but was ${actual.data}"
    )
}

/**
 * Asserts that [actual] is [Result.Failure].
 */
fun <E : AppError> assertFailure(
    actual: Result<*, E>,
    message: String? = null
) {
    contract { returns() implies (actual is Result.Failure<E>) }
    assertIs<Result.Failure<E>>(actual, message ?: "Expected failure to be Result.Failure.")
}

/**
 * Asserts that [actual] is [Result.Failure] and its error is of type [E].
 *
 * Use when you only need to validate the error type, not its exact instance.
 */
inline fun <reified E : AppError> assertFailureIs(
    actual: Result<*, *>,
    message: String? = null
) {
    contract { returns() implies (actual is Result.Failure<*>) }
    assertFailure(actual, message)
    assertIs<E>(
        value = actual.error,
        message = message ?: "Expected error of type ${E::class.simpleName}"
    )
}

/**
 * Asserts that [actual] is [Result.Failure] with error equal to [expectedError].
 *
 * Validates both the error type and value equality. Prefer this when you compare
 * exact error instances (for example, a sealed object or a data class value).
 */
inline fun <reified E : AppError> assertFailureEquals(
    expectedError: E,
    actual: Result<*, E>,
    message: String? = null
) {
    contract { returns() implies (actual is Result.Failure<E>) }
    assertFailure(actual, message)
    assertEquals(
        expected = expectedError,
        actual = actual.error,
        message = message ?: "Expected error to be $expectedError, but was ${actual.error}"
    )
}
