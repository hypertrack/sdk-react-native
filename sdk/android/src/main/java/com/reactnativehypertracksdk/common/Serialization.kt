package com.reactnativehypertracksdk.common

import android.location.Location
import com.hypertrack.sdk.android.HyperTrack
import com.hypertrack.sdk.android.Result

/**
 * Platform-independent serialization code that converts HyperTrack data types
 * to Map<String, T> or List<T> where T is any JSON-compatible type
 */
internal object Serialization {
    fun deserializeAllowMockLocation(allowMockLocation: Serialized): WrapperResult<Boolean> =
        parse(allowMockLocation) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_ALLOW_MOCK_LOCATION)
            it
                .get<Boolean>(KEY_VALUE)
                .getOrThrow()
        }

    fun deserializeDynamicPublishableKey(args: Serialized): WrapperResult<String> =
        parse(args) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_DYNAMIC_PUBLISHABLE_KEY)
            it
                .get<String>(KEY_VALUE)
                .getOrThrow()
        }

    fun deserializeGeotagData(map: Serialized): WrapperResult<GeotagData> =
        parse(map) {
            val data =
                it
                    .get<Serialized>(KEY_GEOTAG_DATA)
                    .getOrThrow()
            val expectedLocationData =
                it
                    .getOptional<Serialized>(KEY_GEOTAG_EXPECTED_LOCATION)
                    .getOrThrow()
            val expectedLocation =
                expectedLocationData?.let {
                    deserializeLocation(it).getOrThrow()
                }
            val orderHandleData =
                it
                    .getOptional<Serialized>(KEY_GEOTAG_ORDER_HANDLE)
                    .getOrThrow()
            val orderHandle = orderHandleData?.let { deserializeOrderHandle(it).getOrThrow() }
            val orderStatusData =
                it
                    .getOptional<Serialized>(KEY_GEOTAG_ORDER_STATUS)
                    .getOrThrow()
            val orderStatus = orderStatusData?.let { deserializeOrderStatus(it).getOrThrow() }
            GeotagData(
                data = data,
                expectedLocation = expectedLocation,
                orderHandle = orderHandle,
                orderStatus = orderStatus,
            )
        }

    fun deserializeIsAvailable(isAvailable: Serialized): WrapperResult<Boolean> =
        parse(isAvailable) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_IS_AVAILABLE)
            it
                .get<Boolean>(KEY_VALUE)
                .getOrThrow()
        }

    fun deserializeIsTracking(isTracking: Serialized): WrapperResult<Boolean> =
        parse(isTracking) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_IS_TRACKING)
            it
                .get<Boolean>(KEY_VALUE)
                .getOrThrow()
        }

    fun deserializeMetadata(metadata: Serialized): WrapperResult<Serialized> =
        parse(metadata) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_METADATA)
            it
                .get<Serialized>(KEY_VALUE)
                .getOrThrow()
        }

    fun deserializeName(name: Serialized): WrapperResult<String> =
        parse(name) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_NAME)
            it
                .get<String>(KEY_VALUE)
                .getOrThrow()
        }

    fun deserializeOrderHandle(map: Serialized): WrapperResult<String> =
        parse(map) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_ORDER_HANDLE)
            it
                .get<String>(KEY_VALUE)
                .getOrThrow()
        }

    fun deserializeWorkerHandle(map: Serialized): WrapperResult<String> =
        parse(map) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_WORKER_HANDLE)
            it
                .get<String>(KEY_VALUE)
                .getOrThrow()
        }

    fun serializeAllowMockLocation(allowMockLocation: Boolean): Serialized =
        mapOf(
            KEY_TYPE to TYPE_ALLOW_MOCK_LOCATION,
            KEY_VALUE to allowMockLocation,
        )

    fun serializeDeviceId(deviceId: String): Serialized =
        mapOf(
            KEY_TYPE to TYPE_DEVICE_ID,
            KEY_VALUE to deviceId,
        )

    fun serializeDynamicPublishableKey(publishableKey: String): Serialized =
        mapOf(
            KEY_TYPE to TYPE_DYNAMIC_PUBLISHABLE_KEY,
            KEY_VALUE to publishableKey,
        )

    fun serializeError(error: HyperTrack.Error): Map<String, String> =
        mapOf(
            KEY_TYPE to TYPE_ERROR,
            KEY_VALUE to
                when (error) {
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
                },
        )

    fun serializeErrors(errors: Set<HyperTrack.Error>): List<Map<String, String>> =
        errors.map {
            serializeError(it)
        }

    fun serializeFailure(failure: List<Serialized>): Serialized =
        mapOf(
            KEY_TYPE to TYPE_RESULT_FAILURE,
            KEY_VALUE to failure,
        )

    fun serializeFailure(failure: Serialized): Serialized =
        mapOf(
            KEY_TYPE to TYPE_RESULT_FAILURE,
            KEY_VALUE to failure,
        )

    fun serializeIsAvailable(isAvailable: Boolean): Serialized =
        mapOf(
            KEY_TYPE to TYPE_IS_AVAILABLE,
            KEY_VALUE to isAvailable,
        )

    fun serializeIsInsideGeofence(isInsideGeofence: Result<Boolean, HyperTrack.LocationError>): Serialized =
        when (isInsideGeofence) {
            is Result.Failure -> {
                serializeFailure(serializeLocationError(isInsideGeofence.failure))
            }

            is Result.Success -> {
                serializeSuccess(
                    mapOf(
                        KEY_TYPE to TYPE_IS_INSIDE_GEOFENCE,
                        KEY_VALUE to isInsideGeofence.success,
                    ),
                )
            }
        }

    fun serializeIsTracking(isTracking: Boolean): Serialized =
        mapOf(
            KEY_TYPE to TYPE_IS_TRACKING,
            KEY_VALUE to isTracking,
        )

    fun serializeLocateResult(locationResult: Result<HyperTrack.Location, Set<HyperTrack.Error>>): Serialized =
        when (locationResult) {
            is Result.Failure -> {
                serializeFailure(serializeErrors(locationResult.failure))
            }

            is Result.Success -> {
                serializeLocationSuccess(locationResult.success)
            }
        }

    fun serializeLocationResult(locationResult: Result<HyperTrack.Location, HyperTrack.LocationError>): Serialized =
        when (locationResult) {
            is Result.Failure -> {
                serializeLocationErrorFailure(locationResult.failure)
            }

            is Result.Success -> {
                serializeLocationSuccess(locationResult.success)
            }
        }

    fun serializeLocationErrorFailure(locationError: HyperTrack.LocationError): Serialized = serializeFailure(serializeLocationError(locationError))

    fun serializeLocationSuccess(location: HyperTrack.Location): Serialized = serializeSuccess(serializeLocation(location))

    fun serializeLocationWithDeviationSuccess(locationWithDeviation: HyperTrack.LocationWithDeviation): Serialized =
        serializeSuccess(
            serializeLocationWithDeviation(
                locationWithDeviation,
            ),
        )

    fun serializeMetadata(metadata: Serialized): Serialized =
        mapOf(
            KEY_TYPE to TYPE_METADATA,
            KEY_VALUE to metadata,
        )

    fun serializeName(name: String): Serialized =
        mapOf(
            KEY_TYPE to TYPE_NAME,
            KEY_VALUE to name,
        )

    fun serializeOrders(orders: Collection<HyperTrack.Order>): Serialized =
        mapOf(
            KEY_TYPE to TYPE_ORDERS,
            KEY_VALUE to
                orders.mapIndexed { index, order ->
                    mapOf(
                        KEY_ORDER_HANDLE to order.orderHandle,
                        KEY_ORDER_INDEX to index,
                        // beware not to call isInsideGeofence here, it's a computed property
                    )
                },
        )

    fun serializeSuccess(success: Serialized): Serialized =
        mapOf(
            KEY_TYPE to TYPE_RESULT_SUCCESS,
            KEY_VALUE to success,
        )

    fun serializeWorkerHandle(workerHandle: String): Serialized =
        mapOf(
            KEY_TYPE to TYPE_WORKER_HANDLE,
            KEY_VALUE to workerHandle,
        )

    private fun deserializeLocation(map: Serialized): WrapperResult<Location> =
        parse(map) {
            it.assertValue<String>(key = KEY_TYPE, value = TYPE_LOCATION)
            val value =
                it
                    .get<Serialized>(KEY_VALUE)
                    .getOrThrow()
            parse(value) { parser ->
                val latitude =
                    parser
                        .get<Double>(KEY_LATITUDE)
                        .getOrThrow()
                val longitude =
                    parser
                        .get<Double>(KEY_LONGITUDE)
                        .getOrThrow()
                Location("api").also {
                    it.latitude = latitude
                    it.longitude = longitude
                }
            }.getOrThrow()
        }

    private fun deserializeOrderStatus(map: Serialized): WrapperResult<HyperTrack.OrderStatus> =
        parse(map) {
            when (it.get<String>(KEY_TYPE).getOrThrow()) {
                TYPE_GEOTAG_ORDER_STATUS_CLOCK_IN -> HyperTrack.OrderStatus.ClockIn
                TYPE_GEOTAG_ORDER_STATUS_CLOCK_OUT -> HyperTrack.OrderStatus.ClockOut
                TYPE_GEOTAG_ORDER_STATUS_CUSTOM ->
                    HyperTrack.OrderStatus.Custom(
                        it.get<String>(KEY_VALUE).getOrThrow(),
                    )

                else -> throw Error("Unknown order status: $map")
            }
        }

    private fun serializeLocation(location: HyperTrack.Location): Serialized =
        mapOf(
            KEY_TYPE to TYPE_LOCATION,
            KEY_VALUE to
                mapOf(
                    KEY_LATITUDE to location.latitude,
                    KEY_LONGITUDE to location.longitude,
                ),
        )

    private fun serializeLocationWithDeviation(locationWithDeviation: HyperTrack.LocationWithDeviation): Serialized =
        mapOf(
            KEY_TYPE to TYPE_LOCATION_WITH_DEVIATION,
            KEY_VALUE to
                mapOf(
                    KEY_LOCATION to serializeLocation(locationWithDeviation.location),
                    KEY_DEVIATION to locationWithDeviation.deviation,
                ),
        )

    private fun serializeLocationError(locationError: HyperTrack.LocationError): Serialized =
        when (locationError) {
            HyperTrack.LocationError.NotRunning -> {
                mapOf(KEY_TYPE to TYPE_LOCATION_ERROR_NOT_RUNNING)
            }

            HyperTrack.LocationError.Starting -> {
                mapOf(KEY_TYPE to TYPE_LOCATION_ERROR_STARTING)
            }

            is HyperTrack.LocationError.Errors -> {
                mapOf(
                    KEY_TYPE to TYPE_LOCATION_ERROR_ERRORS,
                    KEY_VALUE to
                        locationError.errors
                            .map { serializeError(it) },
                )
            }
        }

    fun <T> parse(
        source: Serialized,
        parseFunction: (Parser) -> T,
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
                },
            )
        }
    }

    internal class Parser(
        private val source: Serialized,
    ) {
        private val _exceptions = mutableListOf<Exception>()
        val exceptions: List<Exception> = _exceptions

        inline fun <reified T> get(key: String): WrapperResult<T> =
            try {
                Success(source[key]!! as T)
            } catch (e: Exception) {
                Failure(
                    ParsingException(key, e)
                        .also {
                            _exceptions.add(it)
                        },
                )
            }

        inline fun <reified T> getOptional(key: String): WrapperResult<T?> =
            try {
                Success(source[key] as T?)
            } catch (e: Exception) {
                Failure(
                    ParsingException(key, e)
                        .also {
                            _exceptions.add(it)
                        },
                )
            }

        inline fun <reified T> assertValue(
            key: String,
            value: Any,
        ) {
            if (source[key] != value) {
                _exceptions.add(Exception("Assertion failed: $key != $value"))
            }
        }
    }

    internal data class ParsingExceptions(
        val source: Any,
        val exceptions: List<Exception>,
    ) : Throwable(
            exceptions
                .joinToString("\n")
                .let {
                    "Invalid input:\n\n${source}\n\n$it"
                },
        )

    internal class ParsingException(
        key: String,
        exception: Exception,
    ) : Exception("Invalid value for '$key': $exception", exception)

    private const val KEY_TYPE = "type"
    private const val KEY_VALUE = "value"

    private const val TYPE_RESULT_FAILURE = "failure"
    private const val TYPE_RESULT_SUCCESS = "success"

    private const val TYPE_ALLOW_MOCK_LOCATION = "allowMockLocation"
    private const val TYPE_DEVICE_ID = "deviceID"
    private const val TYPE_DYNAMIC_PUBLISHABLE_KEY = "dynamicPublishableKey"
    private const val TYPE_ERROR = "error"
    private const val TYPE_IS_AVAILABLE = "isAvailable"
    private const val TYPE_IS_INSIDE_GEOFENCE = "isInsideGeofence"
    private const val TYPE_IS_TRACKING = "isTracking"
    private const val TYPE_LOCATION = "location"
    private const val TYPE_LOCATION_WITH_DEVIATION = "locationWithDeviation"
    private const val TYPE_METADATA = "metadata"
    private const val TYPE_NAME = "name"
    private const val TYPE_ORDERS = "orders"
    private const val TYPE_WORKER_HANDLE = "workerHandle"

    private const val TYPE_LOCATION_ERROR_ERRORS = "errors"
    private const val TYPE_LOCATION_ERROR_NOT_RUNNING = "notRunning"
    private const val TYPE_LOCATION_ERROR_STARTING = "starting"

    private const val TYPE_ORDER_HANDLE = "orderHandle"
    private const val TYPE_GEOTAG_ORDER_STATUS_CLOCK_IN = "orderStatusClockIn"
    private const val TYPE_GEOTAG_ORDER_STATUS_CLOCK_OUT = "orderStatusClockOut"
    private const val TYPE_GEOTAG_ORDER_STATUS_CUSTOM = "orderStatusCustom"

    private const val KEY_LATITUDE = "latitude"
    private const val KEY_LONGITUDE = "longitude"

    private const val KEY_DEVIATION = "deviation"
    private const val KEY_GEOTAG_DATA = "data"
    private const val KEY_GEOTAG_EXPECTED_LOCATION = "expectedLocation"
    private const val KEY_GEOTAG_ORDER_HANDLE = "orderHandle"
    private const val KEY_GEOTAG_ORDER_STATUS = "orderStatus"
    private const val KEY_LOCATION = "location"
    private const val KEY_ORDER_HANDLE = "orderHandle"
    private const val KEY_ORDER_INDEX = "index"
}
