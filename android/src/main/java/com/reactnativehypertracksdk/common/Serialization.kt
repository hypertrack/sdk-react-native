package com.reactnativehypertracksdk.common

import android.location.Location

/**
 * Platform-independent serialization code that converts HyperTrack data types
 * to Map<String, T> or List<T> where T is any JSON-compatible type
 */
internal object Serialization {

    fun serializeErrors(errors: Set<HyperTrackError>): List<Map<String, String>> {
        return errors.map {
            serializeHypertrackError(it)
        }
    }

    fun serializeSuccess(location: Location): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_RESULT_SUCCESS,
            KEY_VALUE to serializeLocation(location)
        )
    }

    fun serializeFailure(locationError: LocationError): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_RESULT_FAILURE,
            KEY_VALUE to serializeLocationError(locationError)
        )
    }

    fun serializeIsTracking(isTracking: Boolean): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_IS_TRACKING,
            KEY_VALUE to isTracking
        )
    }

    fun serializeIsAvailable(isAvailable: Boolean): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_AVAILABILITY,
            KEY_VALUE to isAvailable
        )
    }

    fun serializeLocation(location: Location): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_LOCATION,
            KEY_VALUE to mapOf(
                KEY_LATITUDE to location.latitude,
                KEY_LONGITUDE to location.longitude
            )
        )
    }

    fun serializeLocationError(locationError: LocationError): Map<String, Any?> {
        return when (locationError) {
            NotRunning -> {
                mapOf(KEY_TYPE to TYPE_LOCATION_ERROR_NOT_RUNNING)
            }
            Starting -> {
                mapOf(KEY_TYPE to TYPE_LOCATION_ERROR_STARTING)
            }
            is Errors -> {
                mapOf(
                    KEY_TYPE to TYPE_LOCATION_ERROR_ERRORS,
                    KEY_VALUE to locationError.errors
                        .map { serializeHypertrackError(it) }
                )
            }
        }
    }

    fun serializeHypertrackError(error: HyperTrackError): Map<String, String> {
        return mapOf(
            KEY_TYPE to TYPE_HYPERTRACK_ERROR,
            KEY_VALUE to error.name
        )
    }

    fun serializeDeviceId(deviceId: String): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_DEVICE_ID,
            KEY_VALUE to deviceId
        )
    }

    fun deserializeDeviceName(name: Map<String, Any?>): Result<String> {
        return parse(name) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_DEVICE_NAME)
            it
                .get<String>(KEY_VALUE)
                .getOrThrow()
        }
    }

    fun deserializeAvailability(isAvailable: Map<String, Any?>): Result<Boolean> {
        return parse(isAvailable) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_AVAILABILITY)
            it
                .get<Boolean>(KEY_VALUE)
                .getOrThrow()
        }
    }

    fun deserializeGeotagData(map: Map<String, Any?>): Result<Geotag> {
        return parse(map) {
            val data = it
                .get<Map<String, Any?>>(KEY_GEOTAG_DATA)
                .getOrThrow()
            Geotag(data)
        }
    }

    fun <T> parse(
        source: Map<String, Any?>,
        parseFunction: (Parser) -> T
    ): Result<T> {
        val parser = Parser(source)
        return try {
            if (parser.exceptions.isEmpty()) {
                Success(parseFunction.invoke(parser))
            } else {
                Failure(ParsingExceptions(source, parser.exceptions))
            }
        } catch (e: Exception) {
            Failure(
                if (parser.exceptions.isNotEmpty()) {
                    ParsingExceptions(source, parser.exceptions + e)
                } else {
                    e
                }
            )
        }
    }

    internal class Parser(
        private val source: Map<String, Any?>
    ) {
        private val _exceptions = mutableListOf<Exception>()
        val exceptions: List<Exception> = _exceptions

        inline fun <reified T> get(
            key: String
        ): Result<T> {
            return try {
                Success(source[key]!! as T)
            } catch (e: Exception) {
                Failure(
                    ParsingException(key, e)
                        .also {
                            _exceptions.add(it)
                        }
                )
            }
        }

        inline fun <reified T> assertValue(
            key: String,
            value: Any
        ) {
            if (source[key] != value) {
                _exceptions.add(Exception("Assertion failed: $key != $value"))
            }
        }
    }

    internal data class ParsingExceptions(
        val source: Any,
        val exceptions: List<Exception>
    ) : Throwable(
        exceptions.joinToString("\n")
            .let {
                "Invalid input:\n\n${source}\n\n$it"
            }
    )

    internal class ParsingException(
        key: String,
        exception: Exception
    ) : Exception("Invalid value for '$key': $exception", exception)

    private const val KEY_TYPE = "type"
    private const val KEY_VALUE = "value"

    private const val TYPE_RESULT_SUCCESS = "success"
    private const val TYPE_RESULT_FAILURE = "failure"

    private const val TYPE_LOCATION = "location"
    private const val TYPE_AVAILABILITY = "isAvailable"
    private const val TYPE_DEVICE_NAME = "deviceName"
    private const val TYPE_DEVICE_ID = "deviceID"
    private const val TYPE_HYPERTRACK_ERROR = "hyperTrackError"
    private const val TYPE_IS_TRACKING = "isTracking"

    private const val TYPE_LOCATION_ERROR_NOT_RUNNING = "notRunning"
    private const val TYPE_LOCATION_ERROR_STARTING = "starting"
    private const val TYPE_LOCATION_ERROR_ERRORS = "errors"

    private const val KEY_LATITUDE = "latitude"
    private const val KEY_LONGITUDE = "longitude"

    const val KEY_GEOTAG_DATA = "data"
}
