package com.reactnativehypertracksdk.common

import android.location.Location
import com.hypertrack.sdk.android.HyperTrack
import com.hypertrack.sdk.android.Result

/**
 * Platform-independent serialization code that converts HyperTrack data types
 * to Map<String, T> or List<T> where T is any JSON-compatible type
 */
internal object Serialization {

    fun deserializeIsAvailable(isAvailable: Map<String, Any?>): WrapperResult<Boolean> {
        return parse(isAvailable) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_IS_AVAILABLE)
            it
                .get<Boolean>(KEY_VALUE)
                .getOrThrow()
        }
    }

    fun deserializeIsTracking(isTracking: Map<String, Any?>): WrapperResult<Boolean> {
        return parse(isTracking) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_IS_TRACKING)
            it
                .get<Boolean>(KEY_VALUE)
                .getOrThrow()
        }
    }

    fun deserializeMetadata(metadata: Map<String, Any?>): WrapperResult<Map<String, Any?>> {
        return parse(metadata) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_METADATA)
            it
                .get<Map<String, Any?>>(KEY_VALUE)
                .getOrThrow()
        }
    }

    fun deserializeName(name: Map<String, Any?>): WrapperResult<String> {
        return parse(name) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_NAME)
            it
                .get<String>(KEY_VALUE)
                .getOrThrow()
        }
    }

    fun deserializeGeotagData(map: Map<String, Any?>): WrapperResult<GeotagData> {
        return parse(map) {
            val data = it
                .get<Map<String, Any?>>(KEY_GEOTAG_DATA)
                .getOrThrow()
            val locationData = it
                .getOptional<Map<String, Any?>>(KEY_GEOTAG_EXPECTED_LOCATION)
                .getOrThrow()
            val location = locationData?.let { deserializeLocation(it).getOrThrow() }
            GeotagData(data, location)
        }
    }

    fun serializeDeviceId(deviceId: String): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_DEVICE_ID,
            KEY_VALUE to deviceId
        )
    }

    fun serializeError(error: HyperTrack.Error): Map<String, String> {
        return mapOf(
            KEY_TYPE to TYPE_ERROR,
            KEY_VALUE to when (error) {
                HyperTrack.Error.BlockedFromRunning -> "blockedFromRunning"
                HyperTrack.Error.InvalidPublishableKey -> "invalidPublishableKey"
                HyperTrack.Error.Location.Mocked -> "location.mocked"
                HyperTrack.Error.Location.ServicesDisabled -> "location.servicesDisabled"
                HyperTrack.Error.Location.ServicesUnavailable -> "location.servicesUnavailable"
                HyperTrack.Error.Location.SignalLost -> "location.signalLost"
                HyperTrack.Error.NoExemptionFromBackgroundStartRestrictions -> "noExemptionFromBackgroundStartRestrictions"
                HyperTrack.Error.Permissions.Location.Denied -> "permissions.location.denied"
                HyperTrack.Error.Permissions.Location.InsufficientForBackground -> "permissions.location.insufficientForBackground"
                HyperTrack.Error.Permissions.Location.ReducedAccuracy -> "permissions.location.reducedAccuracy"
                HyperTrack.Error.Permissions.Notifications.Denied -> "permissions.notifications.denied"
            }
        )
    }

    fun serializeErrors(errors: Set<HyperTrack.Error>): List<Map<String, String>> {
        return errors.map {
            serializeError(it)
        }
    }

    fun serializeIsAvailable(isAvailable: Boolean): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_IS_AVAILABLE,
            KEY_VALUE to isAvailable
        )
    }

    fun serializeIsTracking(isTracking: Boolean): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_IS_TRACKING,
            KEY_VALUE to isTracking
        )
    }

    fun serializeMetadata(metadata: Map<String, Any?>): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_METADATA,
            KEY_VALUE to metadata
        )
    }

    fun serializeName(name: String): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_NAME,
            KEY_VALUE to name
        )
    }

    fun serializeLocateResult(
        locationResult: Result<HyperTrack.Location, Set<HyperTrack.Error>>
    ): Map<String, Any?> {
        return when (locationResult) {
            is Result.Failure -> {
                serializeFailure(serializeErrors(locationResult.failure))
            }

            is Result.Success -> {
                serializeLocationSuccess(locationResult.success)
            }
        }
    }

    fun serializeLocationResult(
        locationResult: Result<HyperTrack.Location, HyperTrack.LocationError>
    ): Map<String, Any?> {
        return when (locationResult) {
            is Result.Failure -> {
                serializeLocationErrorFailure(locationResult.failure)
            }

            is Result.Success -> {
                serializeLocationSuccess(locationResult.success)
            }
        }
    }

    fun serializeLocationErrorFailure(locationError: HyperTrack.LocationError): Map<String, Any?> {
        return serializeFailure(serializeLocationError(locationError))
    }

    fun serializeLocationSuccess(location: HyperTrack.Location): Map<String, Any?> {
        return serializeSuccess(serializeLocation(location))
    }

    fun serializeLocationWithDeviationSuccess(
        locationWithDeviation: HyperTrack.LocationWithDeviation,
    ): Map<String, Any?> {
        return serializeSuccess(
            serializeLocationWithDeviation(
                locationWithDeviation
            )
        )
    }

    private fun serializeLocationWithDeviation(
        locationWithDeviation: HyperTrack.LocationWithDeviation,
    ): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_LOCATION_WITH_DEVIATION,
            KEY_VALUE to mapOf(
                KEY_LOCATION to serializeLocation(locationWithDeviation.location),
                KEY_DEVIATION to locationWithDeviation.deviation
            )
        )
    }

    private fun deserializeLocation(map: Map<String, Any?>): WrapperResult<Location> {
        return parse(map) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_LOCATION)
            val value = it
                .get<Map<String, Any?>>(KEY_VALUE)
                .getOrThrow()
            parse(value) { parser ->
                val latitude = parser
                    .get<Double>(KEY_LATITUDE)
                    .getOrThrow()
                val longitude = parser
                    .get<Double>(KEY_LONGITUDE)
                    .getOrThrow()
                Location("api").also {
                    it.latitude = latitude
                    it.longitude = longitude
                }
            }.getOrThrow()
        }
    }

    private fun serializeFailure(failure: List<Map<String, Any?>>): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_RESULT_FAILURE,
            KEY_VALUE to failure
        )
    }

    private fun serializeFailure(failure: Map<String, Any?>): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_RESULT_FAILURE,
            KEY_VALUE to failure
        )
    }

    private fun serializeSuccess(success: Map<String, Any?>): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_RESULT_SUCCESS,
            KEY_VALUE to success
        )
    }

    fun serializeLocation(location: HyperTrack.Location): Map<String, Any?> {
        return mapOf(
            KEY_TYPE to TYPE_LOCATION,
            KEY_VALUE to mapOf(
                KEY_LATITUDE to location.latitude,
                KEY_LONGITUDE to location.longitude
            )
        )
    }


    private fun serializeLocationError(locationError: HyperTrack.LocationError): Map<String, Any?> {
        return when (locationError) {
            HyperTrack.LocationError.NotRunning -> {
                mapOf(KEY_TYPE to TYPE_LOCATION_ERROR_NOT_RUNNING)
            }

            HyperTrack.LocationError.Starting -> {
                mapOf(KEY_TYPE to TYPE_LOCATION_ERROR_STARTING)
            }

            is HyperTrack.LocationError.Errors -> {
                mapOf(
                    KEY_TYPE to TYPE_LOCATION_ERROR_ERRORS,
                    KEY_VALUE to locationError.errors
                        .map { serializeError(it) }
                )
            }
        }
    }

    fun <T> parse(
        source: Map<String, Any?>,
        parseFunction: (Parser) -> T
    ): WrapperResult<T> {
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
        ): WrapperResult<T> {
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

        inline fun <reified T> getOptional(
            key: String
        ): WrapperResult<T?> {
            return try {
                Success(source[key] as T?)
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

    private const val TYPE_RESULT_FAILURE = "failure"
    private const val TYPE_RESULT_SUCCESS = "success"

    private const val TYPE_DEVICE_ID = "deviceID"
    private const val TYPE_ERROR = "error"
    private const val TYPE_IS_AVAILABLE = "isAvailable"
    private const val TYPE_IS_TRACKING = "isTracking"
    private const val TYPE_METADATA = "metadata"
    private const val TYPE_NAME = "name"
    private const val TYPE_LOCATION = "location"
    private const val TYPE_LOCATION_WITH_DEVIATION = "locationWithDeviation"

    private const val TYPE_LOCATION_ERROR_ERRORS = "errors"
    private const val TYPE_LOCATION_ERROR_NOT_RUNNING = "notRunning"
    private const val TYPE_LOCATION_ERROR_STARTING = "starting"

    private const val KEY_LATITUDE = "latitude"
    private const val KEY_LONGITUDE = "longitude"

    const val KEY_DEVIATION = "deviation"
    const val KEY_GEOTAG_DATA = "data"
    const val KEY_GEOTAG_EXPECTED_LOCATION = "expectedLocation"
    const val KEY_LOCATION = "location"
}
