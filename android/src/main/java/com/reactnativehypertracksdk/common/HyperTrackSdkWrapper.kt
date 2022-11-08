package com.reactnativehypertracksdk.common

import com.hypertrack.sdk.*
import com.reactnativehypertracksdk.common.Serialization.deserializeAvailability
import com.reactnativehypertracksdk.common.Serialization.deserializeGeotagData
import com.reactnativehypertracksdk.common.Serialization.serializeFailure
import com.reactnativehypertracksdk.common.Serialization.serializeErrors
import com.reactnativehypertracksdk.common.Serialization.serializeIsAvailable
import com.reactnativehypertracksdk.common.Serialization.serializeIsTracking
import com.reactnativehypertracksdk.common.Serialization.serializeSuccess
import com.reactnativehypertracksdk.common.Serialization.serializeDeviceId
import com.reactnativehypertracksdk.common.Serialization.deserializeDeviceName
import com.reactnativehypertracksdk.common.Serialization.parse
import java.lang.IllegalStateException
import java.lang.RuntimeException
import android.util.Log


/**
 * This class stores SDK instance, calls HyperTrack SDK methods and serializes responses.
 * It receives serialized params.
 */
internal object HyperTrackSdkWrapper {

    // initialize method is guaranteed to be called (by non-native side)
    // prior to any access to the SDK instance
    private lateinit var sdkInstance: HyperTrack

    fun initialize(
        args: Map<String, Any?>
    ): Result<HyperTrack> {
        return try {
            SdkInitParams.fromMap(args).flatMapSuccess { initParams ->
                sdkInstance = HyperTrack.getInstance(initParams.publishableKey)
                if (initParams.loggingEnabled) {
                    HyperTrack.enableDebugLogging()
                }
                if (initParams.allowMockLocations) {
                    sdkInstance.allowMockLocations()
                }
                this.sdkInstance.backgroundTrackingRequirement(
                    initParams.requireBackgroundTrackingPermission
                )
                Success(sdkInstance)
            }
        } catch (exception: Exception) {
            Failure(Exception("Hypertrack SDK initialization failed.", exception))
        }
    }

    fun getDeviceID(): Result<Map<String, Any?>> {
        return withSdkInstance { sdk ->
            serializeDeviceId(sdk.deviceID)
        }
    }

    fun startTracking(): Result<Unit> {
        return withSdkInstance { sdk ->
            sdk.start()
        }
    }

    fun stopTracking(): Result<Unit> {
        return withSdkInstance { sdk ->
            sdk.stop()
        }
    }

    fun sync(): Result<Unit> {
        return withSdkInstance { sdk ->
            sdk.syncDeviceSettings()
        }
    }

    fun addGeotag(args: Map<String, Any?>): Result<Map<String, Any?>> {
        return deserializeGeotagData(args).flatMapSuccess {
            withSdkInstance { sdk ->
                sdk.addGeotag(it.data).let { result ->
                    when (result) {
                        is GeotagResult.SuccessWithDeviation -> {
                            serializeSuccess(result.deviceLocation)
                        }
                        is GeotagResult.Success -> {
                            serializeSuccess(result.deviceLocation)
                        }
                        is GeotagResult.Error -> {
                            serializeFailure(getLocationError(result.reason))
                        }
                        else -> {
                            throw IllegalArgumentException()
                        }
                    }
                }
            }
        }
    }

    fun isTracking(): Result<Map<String, Any?>> {
        return withSdkInstance { sdk ->
            serializeIsTracking(sdk.isTracking)
        }
    }

    fun isAvailable(): Result<Map<String, Any?>> {
        return withSdkInstance { sdk ->
            serializeIsAvailable(sdk.availability.equals(Availability.AVAILABLE))
        }
    }

    fun setAvailability(args: Map<String, Any?>): Result<Unit> {
        return deserializeAvailability(args).flatMapSuccess { isAvailable ->
            withSdkInstance { sdk ->
                if (isAvailable) {
                    sdk.availability = Availability.AVAILABLE
                } else {
                    sdk.availability = Availability.UNAVAILABLE
                }
            }
        }
    }

    fun setName(args: Map<String, Any?>): Result<Unit> {
        return deserializeDeviceName(args).flatMapSuccess { name ->
            withSdkInstance { sdk ->
                sdk.setDeviceName(name)
                Unit
            }
        }
    }

    fun setMetadata(metadata: Map<String, Any?>): Result<Unit> {
        return withSdkInstance { sdk ->
            sdk.setDeviceMetadata(metadata)
        }
    }

    fun getLocation(): Result<Map<String, Any?>> {
        return withSdkInstance { sdk ->
            sdk.latestLocation.let { result ->
                if (result.isSuccess) {
                    serializeSuccess(result.value)
                } else {
                    serializeFailure(getLocationError(result.error))
                }
            }
        }
    }

    fun getInitialErrors(): List<Map<String, String>> {
        return serializeErrors(getHyperTrackErrorsFromBlockers())
    }

    fun getErrors(error: TrackingError): List<Map<String, String>> {
        return serializeErrors(getTrackingErrors((error)))
    }

    fun <T> withSdkInstance(
        onInstanceCall: (sdk: HyperTrack) -> T
    ): Result<T> {
        return try {
            Success(onInstanceCall(sdkInstance))
        } catch (e: Exception) {
            Failure(e)
        }
    }

    private fun getTrackingErrors(error: TrackingError): Set<HyperTrackError> {
        return when (error.code) {
            TrackingError.INVALID_PUBLISHABLE_KEY_ERROR -> {
                HyperTrackError.invalidPublishableKey
            }
            TrackingError.PERMISSION_DENIED_ERROR -> {
                null
            }
            TrackingError.AUTHORIZATION_ERROR -> {
                HyperTrackError.blockedFromRunning
            }
            TrackingError.GPS_PROVIDER_DISABLED_ERROR -> {
                HyperTrackError.locationServicesDisabled
            }
            TrackingError.UNKNOWN_NETWORK_ERROR -> {
                HyperTrackError.blockedFromRunning
            }
            else -> {
                throw RuntimeException("Unknown tracking error")
            }
        }.let { hyperTrackError ->
            (hyperTrackError?.let { setOf(it) } ?: setOf()) + getHyperTrackErrorsFromBlockers()
        }
    }

    private fun getLocationError(error: OutageReason): LocationError {
        val blockersErrors = getHyperTrackErrorsFromBlockers()
        return when (error) {
            OutageReason.NO_GPS_SIGNAL -> {
                Errors(setOf(HyperTrackError.gpsSignalLost) + blockersErrors)
            }
            OutageReason.MISSING_LOCATION_PERMISSION -> {
                Errors(setOf(HyperTrackError.locationPermissionsDenied) + blockersErrors)
            }
            OutageReason.LOCATION_SERVICE_DISABLED -> {
                Errors(setOf(HyperTrackError.locationServicesDisabled) + blockersErrors)
            }
            OutageReason.MISSING_ACTIVITY_PERMISSION -> {
                Errors(setOf(HyperTrackError.motionActivityPermissionsDenied) + blockersErrors)
            }
            OutageReason.NOT_TRACKING -> {
                NotRunning
            }
            OutageReason.START_HAS_NOT_FINISHED -> {
                Starting
            }
            OutageReason.RESTART_REQUIRED -> {
                throw IllegalStateException("RESTART_REQUIRED must not be returned")
            }
        }
    }

    private fun getLocationError(error: GeotagResult.Error.Reason): LocationError {
        val blockersErrors = getHyperTrackErrorsFromBlockers()
        return when (error) {
            GeotagResult.Error.Reason.NO_GPS_SIGNAL -> {
                Errors(setOf(HyperTrackError.gpsSignalLost) + blockersErrors)
            }
            GeotagResult.Error.Reason.MISSING_LOCATION_PERMISSION -> {
                Errors(setOf(HyperTrackError.locationPermissionsDenied) + blockersErrors)
            }
            GeotagResult.Error.Reason.LOCATION_SERVICE_DISABLED -> {
                Errors(setOf(HyperTrackError.locationServicesDisabled) + blockersErrors)
            }
            GeotagResult.Error.Reason.MISSING_ACTIVITY_PERMISSION -> {
                Errors(setOf(HyperTrackError.motionActivityPermissionsDenied) + blockersErrors)
            }
            GeotagResult.Error.Reason.NOT_TRACKING -> {
                NotRunning
            }
            GeotagResult.Error.Reason.START_HAS_NOT_FINISHED -> {
                Starting
            }
        }
    }

    private fun getHyperTrackErrorsFromBlockers(): Set<HyperTrackError> {
        return HyperTrack.getBlockers()
            .map {
                when (it) {
                    Blocker.LOCATION_PERMISSION_DENIED -> {
                        HyperTrackError.locationPermissionsDenied
                    }
                    Blocker.LOCATION_SERVICE_DISABLED -> {
                        HyperTrackError.locationServicesDisabled
                    }
                    Blocker.ACTIVITY_PERMISSION_DENIED -> {
                        HyperTrackError.motionActivityPermissionsDenied
                    }
                    Blocker.BACKGROUND_LOCATION_DENIED -> {
                        HyperTrackError.locationPermissionsInsufficientForBackground
                    }
                }
            }
            .toSet()
    }

}
