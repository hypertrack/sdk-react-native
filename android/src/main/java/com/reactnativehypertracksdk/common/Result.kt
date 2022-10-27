package com.reactnativehypertracksdk.common

internal sealed class Result<S> {
    fun <T> flatMap(function: (S) -> Result<T>): Result<T> {
        return when (this) {
            is Success -> {
                function.invoke(this.success)
            }
            is Failure -> {
                Failure<T>(this.failure)
            }
        }
    }
}

internal data class Success<S>(val success: S) : Result<S>()
internal data class Failure<S>(val failure: Exception) : Result<S>()
