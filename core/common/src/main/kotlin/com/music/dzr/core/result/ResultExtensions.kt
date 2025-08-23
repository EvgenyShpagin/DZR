@file:OptIn(ExperimentalContracts::class)

package com.music.dzr.core.result

import com.music.dzr.core.error.AppError
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Executes [action] when the receiver is a [Result.Success] and returns
 * the original [Result]. Allows centralized error handling while
 * keeping the chain-style call flow intact.
 */
inline fun <D, E : AppError> Result<D, E>.onSuccess(
    action: (data: D) -> Unit
): Result<D, E> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }

    if (this is Result.Success) {
        action(data)
    }
    return this
}

/**
 * Executes [action] when the receiver is a [Result.Failure] and returns
 * the original [Result]. Allows centralized error handling while
 * keeping the chain-style call flow intact.
 */
inline fun <D, E : AppError> Result<D, E>.onFailure(
    action: (error: E) -> Unit
): Result<D, E> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }

    if (this is Result.Failure) {
        action(error)
    }
    return this
}

/**
 * Returns `true` if this instance represents a successful outcome.
 * In this case [isFailure] returns `false`.
 */
fun <D, E : AppError> Result<D, E>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Result.Success<D>)
        returns(false) implies (this@isSuccess is Result.Failure<E>)
    }
    return this is Result.Success<D>
}

/**
 * Returns `true` if this instance represents a failed outcome.
 * In this case [isSuccess] returns `false`.
 */
fun <D, E : AppError> Result<D, E>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is Result.Failure<E>)
        returns(false) implies (this@isFailure is Result.Success<D>)
    }
    return this is Result.Failure<E>
}