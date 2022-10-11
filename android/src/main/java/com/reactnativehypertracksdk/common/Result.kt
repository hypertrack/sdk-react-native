package com.reactnativehypertracksdk.common

sealed class Result<S>
data class Success<S>(val success: S): Result<S>()
data class Failure<S>(val failure: Exception): Result<S>()
