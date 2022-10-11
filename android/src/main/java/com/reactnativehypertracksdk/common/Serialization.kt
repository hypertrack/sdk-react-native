package com.reactnativehypertracksdk.common

import android.location.Location

/**
 * Platform-independent serialization code that converts HyperTrack data types
 * to Map<String, T> or List<T> where T is any JSON-compatible type
 */

fun serializeErrors(errors: Set<HyperTrackError>): List<Map<String, String>> {
  return errors.map {
    serializeHypertrackError(it)
  }
}

fun serializeSuccess(location: Location): Map<String, Any> {
  return mapOf(
    KEY_TYPE to TYPE_RESULT_SUCCESS,
    KEY_VALUE to serializeLocation(location)
  )
}

fun serializeFailure(locationError: LocationError): Map<String, Any> {
  return mapOf(
    KEY_TYPE to TYPE_RESULT_FAILURE,
    KEY_VALUE to serializeLocationError(locationError)
  )
}

fun serializeIsTracking(isTracking: Boolean): Map<String, Boolean> {
  return mapOf(KEY_IS_TRACKING to isTracking)
}

fun serializeAvailability(isAvailable: Boolean): Map<String, Boolean> {
  return mapOf(KEY_AVAILABILITY to isAvailable)
}

fun deserializeAvailability(isAvailable: Map<String, Boolean>): Boolean {
  return isAvailable.getValue(KEY_AVAILABILITY)
}

fun serializeLocation(location: Location): Map<String, Double> {
  return mapOf(
    KEY_LATITUDE to location.latitude,
    KEY_LONGITUDE to location.longitude
  )
}

fun serializeLocationError(locationError: LocationError): Map<String, Any> {
  return when(locationError) {
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

fun serializeHypertrackError(error: HyperTrackError): Map<String, String> {
  return mapOf(
    KEY_TYPE to error.name,
  )
}

private const val KEY_TYPE = "type"
private const val KEY_VALUE = "value"

private const val TYPE_RESULT_SUCCESS = "success"
private const val TYPE_RESULT_FAILURE = "failure"

private const val TYPE_LOCATION_ERROR_NOT_RUNNING = "notRunning"
private const val TYPE_LOCATION_ERROR_STARTING = "starting"
private const val TYPE_LOCATION_ERROR_ERRORS = "errors"

private const val KEY_AVAILABILITY = "isAvailable"
private const val KEY_IS_TRACKING = "isTracking"
private const val KEY_LATITUDE = "latitude"
private const val KEY_LONGITUDE = "longitude"
const val KEY_GEOTAG_DATA = "data"
