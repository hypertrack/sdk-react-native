package com.reactnativehypertracksdk

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.reactnativehypertracksdk.common.Failure
import com.reactnativehypertracksdk.common.Success
import com.reactnativehypertracksdk.common.WrapperResult

@Suppress("UNCHECKED_CAST")
internal fun <T> WrapperResult<T>.toPromise(promise: Promise) {
    when (this) {
        is Success -> {
            when (this.success) {
                is Map<*, *> -> {
                    promise.resolve((this.success as Map<String, Any>).toWritableMap())
                }

                is List<*> -> {
                    promise.resolve((this.success as List<Any>).toWriteableArray())
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
            /**
             * ReactNative NativeBridge converts the Exception to JS Error
             * adding some additional fields (see com.facebook.react.bridge.PromiseImpl.java):
             *   "nativeStackAndroid": [
             *     {
             *       "lineNumber": 29,
             *       "file": "ReactNativeSerialization.kt",
             *       "methodName": "toPromise",
             *       "class": "com.reactnativehypertracksdk.ReactNativeSerializationKt"
             *     },
             *   ]
             *
             *   "userInfo": {}
             *
             *   Default ReactNative logger skips the Exception class name in logs, so we build a
             *   custom Exception to include the class name in the message.
             *
             *   Default Exception.toString() will print both the class name and the message
             */
            val exception = Exception(this.failure.toString())
            exception.stackTrace = this.failure.stackTrace
            promise.reject(exception)
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

                is Float -> {
                    putDouble(key, value.toDouble())
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
