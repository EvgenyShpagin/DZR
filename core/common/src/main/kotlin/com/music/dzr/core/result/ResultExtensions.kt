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