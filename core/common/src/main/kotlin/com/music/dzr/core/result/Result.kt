package com.music.dzr.core.result


sealed class Result<out D, out E : AppError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>()
    data class Failure<out E : AppError>(val error: E) : Result<Nothing, E>()
}