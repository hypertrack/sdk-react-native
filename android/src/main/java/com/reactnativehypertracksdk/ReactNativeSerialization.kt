package com.reactnativehypertracksdk

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.reactnativehypertracksdk.common.Failure
import com.reactnativehypertracksdk.common.Result
import com.reactnativehypertracksdk.common.Success

@Suppress("UNCHECKED_CAST")
internal fun <T> Result<T>.toPromise(promise: Promise) {
    when (this) {
        is Success -> {
            when (this.success) {
                is Map<*, *> -> {
                    promise.resolve((this.success as Map<String, Any>).toWritableMap())
                }
                is Unit -> {
                    promise.resolve(null)
                }
                else -> {
                    // using nested exception to correctly display error in RN logs
                    promise.reject(Exception(IllegalArgumentException(this.success.toString())))
                }
            }
        }
        is Failure -> {
            promise.reject(Exception(this.failure))
        }
    }
}

@Suppress("UNCHECKED_CAST")
internal fun List<Any>.toWriteableArray(): WritableArray {
    return Arguments.createArray().also { writableArray ->
        forEach {
            when (it) {
                is String -> {
                    writableArray.pushString(it)
                }
                is Map<*, *> -> {
                    writableArray.pushMap((it as Map<String, Any>).toWritableMap())
                }
                else -> {
                    throw Exception(IllegalArgumentException(it.javaClass.toString()))
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
internal fun Map<String, Any?>.toWritableMap(): WritableMap {
    val map = this
    return Arguments.createMap().apply {
        map.forEach { entry ->
            val key = entry.key
            when (val value = entry.value) {
                is String -> {
                    putString(key, value)
                }
                is Int -> {
                    putInt(key, value)
                }
                is Double -> {
                    putDouble(key, value)
                }
                is Boolean -> {
                    putBoolean(key, value)
                }
                is List<*> -> {
                    putArray(key, (value as List<String>).toWriteableArray())
                }
                is Map<*, *> -> {
                    putMap(key, (value as Map<String, Any>).toWritableMap())
                }
                null -> {
                    putNull(key)
                }
                else -> {
                    throw Exception(IllegalArgumentException(entry.value?.javaClass.toString()))
                }
            }
        }
    }
}
