package com.reactnativehypertracksdk.common

import android.location.Location
import java.lang.IllegalArgumentException

/**
 * Platform-independent serialization code that converts HyperTrack data types
 * to Map<String, T> or List<T> where T is any JSON-compatible type
 */

internal fun serializeErrors(errors: Set<HyperTrackError>): List<Map<String, String>> {
    return errors.map {
        serializeHypertrackError(it)
    }
}

internal fun serializeSuccess(location: Location): Map<String, Any> {
    return mapOf(
        KEY_TYPE to TYPE_RESULT_SUCCESS,
        KEY_VALUE to serializeLocation(location)
    )
}

internal fun serializeFailure(locationError: LocationError): Map<String, Any> {
    return mapOf(
        KEY_TYPE to TYPE_RESULT_FAILURE,
        KEY_VALUE to serializeLocationError(locationError)
    )
}

internal fun serializeIsTracking(isTracking: Boolean): Map<String, Any> {
    return mapOf(
        KEY_TYPE to TYPE_IS_TRACKING,
        KEY_VALUE to isTracking
    )
}

internal fun serializeIsAvailable(isAvailable: Boolean): Map<String, Any> {
    return mapOf(
        KEY_TYPE to TYPE_AVAILABILITY,
        KEY_VALUE to isAvailable
    )
}

internal fun serializeLocation(location: Location): Map<String, Any> {
    return mapOf(
        KEY_TYPE to TYPE_LOCATION,
        KEY_VALUE to mapOf(
            KEY_LATITUDE to location.latitude,
            KEY_LONGITUDE to location.longitude
        )
    )
}

internal fun serializeLocationError(locationError: LocationError): Map<String, Any> {
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
                KEY_VALUE to locationError.errors.map { serializeHypertrackError(it) }
            )
        }
    }
}

internal fun serializeHypertrackError(error: HyperTrackError): Map<String, String> {
    return mapOf(
        KEY_TYPE to TYPE_HYPERTRACK_ERROR,
        KEY_VALUE to error.name
    )
}

internal fun deserializeAvailability(isAvailable: Map<String, Any>): Boolean {
    if (isAvailable.getValue(KEY_TYPE) != TYPE_AVAILABILITY) {
        throw IllegalArgumentException(isAvailable.toString())
    }
    return isAvailable.getValue(KEY_VALUE) as Boolean
}

internal fun deserializeGeotagData(map: Map<String, Any>): Map<String, Any> {
    return map.getValue(KEY_GEOTAG_DATA) as Map<String, Any>
}

private const val KEY_TYPE = "type"
private const val KEY_VALUE = "value"

private const val TYPE_RESULT_SUCCESS = "success"
private const val TYPE_RESULT_FAILURE = "failure"

private const val TYPE_LOCATION = "location"
private const val TYPE_AVAILABILITY = "isAvailable"
private const val TYPE_HYPERTRACK_ERROR = "hyperTrackError"
private const val TYPE_IS_TRACKING = "isTracking"

private const val TYPE_LOCATION_ERROR_NOT_RUNNING = "notRunning"
private const val TYPE_LOCATION_ERROR_STARTING = "starting"
private const val TYPE_LOCATION_ERROR_ERRORS = "errors"

private const val KEY_LATITUDE = "latitude"
private const val KEY_LONGITUDE = "longitude"

internal const val KEY_GEOTAG_DATA = "data"
