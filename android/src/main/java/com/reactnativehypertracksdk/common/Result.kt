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

    fun forceUnwrap(): S {
        return when (this) {
            is Success -> this.success
            is Failure -> throw Exception(
                "Unwrapping failed, this Result is failure: ${this.failure}",
                this.failure
            )
        }
    }

    companion object {
        fun <T> tryAsResult(block: () -> T): Result<T> {
            return try {
                Success(block.invoke())
            } catch (e: Exception) {
                Failure(e)
            }
        }
    }
}

internal data class Success<S>(val success: S) : Result<S>()
internal data class Failure<S>(val failure: Exception) : Result<S>()
