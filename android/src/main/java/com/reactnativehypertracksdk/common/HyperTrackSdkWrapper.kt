package com.reactnativehypertracksdk.common

import com.hypertrack.sdk.*
import java.lang.IllegalStateException
import java.lang.RuntimeException


/**
 * This class store SDK instance, calls HyperTrack SDK methods and serializes responses
 */
object HyperTrackSdkWrapper {

  private var sdkInstance: HyperTrack? = null

  fun initialize(
    publishableKey: String,
    initParams: SdkInitParams
  ): Result<HyperTrack> {
    return try {
      sdkInstance = HyperTrack.getInstance(publishableKey)
      withSdkInstance { sdk ->
        if(initParams.loggingEnabled) {
          HyperTrack.enableDebugLogging()
        }
        if(initParams.allowMockLocations) {
          sdk.allowMockLocations()
        }
        sdk.backgroundTrackingRequirement(initParams.requireBackgroundTrackingPermission)
      }
    } catch (exception: Exception) {
      Failure(Exception("Hypertrack SDK initialization failed.", exception))
    }
  }

  fun getDeviceID(): Result<String> {
    return withSdkInstance { sdk ->
      sdk.deviceID
    }
  }

  fun isTracking(): Result<Map<String, Boolean>> {
    return withSdkInstance { sdk ->
      serializeIsTracking(sdk.isTracking)
    }
  }

  fun startTracking() {
    withSdkInstance { sdk ->
      sdk.start()
    }
  }

  fun stopTracking() {
    withSdkInstance { sdk ->
      sdk.stop()
    }
  }

  fun sync() {
    withSdkInstance { sdk ->
      sdk.syncDeviceSettings()
    }
  }

  @Suppress("UNCHECKED_CAST")
  fun addGeotag(map: Map<String, Any>): Result<Map<String, Any>> {
    return withSdkInstance { sdk ->
      sdk.addGeotag(map.getValue(KEY_GEOTAG_DATA) as Map<String, Any>).let { result ->
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

  fun isAvailable(): Result<Map<String, Boolean>> {
    return withSdkInstance { sdk ->
      serializeAvailability(sdk.availability.equals(Availability.AVAILABLE))
    }
  }

  fun setAvailability(availability: Map<String, Boolean>) {
    withSdkInstance { sdk ->
      if (deserializeAvailability(availability)) {
        sdk.availability = Availability.AVAILABLE
      } else {
        sdk.availability = Availability.UNAVAILABLE
      }
    }
  }

  fun setName(name: String) {
    withSdkInstance { sdk ->
      sdk.setDeviceName(name)
    }
  }

  fun setMetadata(metadata: Map<String, Any>) {
    withSdkInstance { sdk ->
      sdk.setDeviceMetadata(metadata)
    }
  }

  fun getLocation(): Result<Map<String, Any>> {
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

  fun getTrackingErrors(error: TrackingError): Set<HyperTrackError> {
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

  fun <T> withSdkInstance(
    onInstanceCall: (sdk: HyperTrack) -> T
  ): Result<T> {
    return this.sdkInstance.let { instance ->
      if (instance == null) {
        Failure(Exception("HyperTrack SDK is not initialized"))
      } else {
        try {
          Success(onInstanceCall(instance))
        } catch (e: Exception) {
          Failure(e)
        }
      }
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
    return HyperTrack.getBlockers().map {
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
    }.toSet()
  }

}
