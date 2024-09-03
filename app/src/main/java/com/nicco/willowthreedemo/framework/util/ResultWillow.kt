package com.nicco.willowthreedemo.framework.util

sealed class ResultWillow<out S, out F> {
    data class Success<out S>(val value: S): ResultWillow<S, Nothing>()
    data class Failure<out F>(val value: F): ResultWillow<Nothing, F>()
}

inline fun <S, F, T> ResultWillow<S, F>.flow(
    success: (S) -> T,
    failure: (F) -> T
): T = when(this) {
    is ResultWillow.Success -> success(value)
    is ResultWillow.Failure -> failure(value)
}