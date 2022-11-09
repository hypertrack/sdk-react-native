package com.reactnativehypertracksdk.common

internal sealed class Result<S> {
    fun <T> flatMapSuccess(onSuccess: (S) -> Result<T>): Result<T> {
        return when (this) {
            is Success -> {
                onSuccess.invoke(this.success)
            }
            is Failure -> {
                Failure<T>(this.failure)
            }
        }
    }

    fun <T> mapSuccess(onSuccess: (S) -> T): Result<T> {
        return when (this) {
            is Success -> {
                Success(onSuccess.invoke(this.success))
            }
            is Failure -> {
                Failure<T>(this.failure)
            }
        }
    }

    fun getOrThrow(): S {
        return when (this) {
            is Success -> this.success
            is Failure -> throw Exception(
                "Result unwrapping failed: ${this.failure}",
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
internal data class Failure<S>(val failure: Throwable) : Result<S>()
