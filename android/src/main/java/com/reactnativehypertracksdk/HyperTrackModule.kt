package com.hypertrack.reactnative.androidsdk

import android.location.Location
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.hypertrack.sdk.*
import com.hypertrack.sdk.TrackingStateObserver.OnTrackingStateChangeListener
import java.util.*

@ReactModule(name = HTSDKModule.NAME)
class HTSDKModule(reactContext: ReactApplicationContext?) :
  ReactContextBaseJavaModule(reactContext) {
  var trackingStateChangeListener: OnTrackingStateChangeListener? = null
  var sdkInstance: HyperTrack? = null
  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun addListener(type: String?) {
    // Keep: Required for RN built in Event Emitter Calls.
  }

  @ReactMethod
  fun removeListeners(type: Int?) {
    // Keep: Required for RN built in Event Emitter Calls.
  }

  @ReactMethod
  fun initialize(
    publishableKey: String?,
    startsTracking: Boolean,
    automaticallyRequestPermissions: Boolean?,
    promise: Promise
  ) {
    try {
      sdkInstance = HyperTrack.getInstance(
        publishableKey!!
      ).backgroundTrackingRequirement(false)
      subscribeToTracking()
      if (startsTracking) {
        sdkInstance!!.start()
      }
      promise.resolve(true)
    } catch (exception: Exception) {
      Log.e(TAG, "Hypertrack SDK initialization failed.", exception)
      val error = String.format(Locale.ENGLISH, "%d", RN_ERROR_INVALID_PUBLISHABLE_KEY)
      promise.reject(error, Exception("Hypertrack SDK initialization failed."))
    }
  }

  fun subscribeToTracking() {
    if (trackingStateChangeListener != null) {
      sdkInstance!!.removeTrackingListener(trackingStateChangeListener)
      trackingStateChangeListener = null
    }
    trackingStateChangeListener = object : OnTrackingStateChangeListener {
      override fun onError(trackingError: TrackingError) {

        val params = Arguments.createMap()
        params.putInt("code", convertToRNErrorsCode(trackingError.code))
        params.putString("message", trackingError.message)
        reactApplicationContext
          .getJSModule(RCTDeviceEventEmitter::class.java)
          .emit("onTrackingErrorHyperTrack", params)
      }

      override fun onTrackingStart() {
        reactApplicationContext
          .getJSModule(RCTDeviceEventEmitter::class.java)
          .emit("onTrackingStateChanged", sdkInstance!!.isRunning)
      }

      override fun onTrackingStop() {
        reactApplicationContext
          .getJSModule(RCTDeviceEventEmitter::class.java)
          .emit("onTrackingStateChanged", sdkInstance!!.isRunning)
      }
    }
    sdkInstance!!.addTrackingListener(trackingStateChangeListener)
  }

  @ReactMethod
  fun getDeviceID(promise: Promise) {
    promise.resolve(sdkInstance!!.deviceID)
  }

  @ReactMethod
  fun isTracking(promise: Promise) {
    promise.resolve(sdkInstance!!.isRunning)
  }

  @ReactMethod
  fun startTracking() {
    sdkInstance!!.start()
  }

  @ReactMethod
  fun stopTracking() {
    sdkInstance!!.stop()
  }

  @ReactMethod
  fun syncDeviceSettings() {
    sdkInstance!!.syncDeviceSettings()
  }

  @ReactMethod
  fun addGeotag(rMap: ReadableMap, promise: Promise) {
    try {
      sdkInstance!!.addGeotag(rMap.toHashMap())
      promise.resolve(locationResultToMap(sdkInstance!!.latestLocation))
    } catch (e: Exception) {
      val error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL)
      promise.reject(error, e)
    }
  }

  @ReactMethod
  fun isAvailable(promise: Promise) {
    promise.resolve(sdkInstance!!.availability.equals(Availability.AVAILABLE))
  }


  @ReactMethod
  fun setAvailability(availability: Boolean, promise: Promise) {
    if (availability) {
      sdkInstance!!.availability = Availability.AVAILABLE
    } else {
      sdkInstance!!.availability = Availability.UNAVAILABLE
    }
  }

//  @ReactMethod
//  fun addGeotag(rMap: ReadableMap, expectedLocation: ReadableMap, promise: Promise) {
//    try {
//      val expected = expectedLocationFromMap(expectedLocation)
//      val result = sdkInstance!!.addGeotag(rMap.toHashMap(), expected)
//      if (result is GeotagResult.Success) {
//        val deviceLocation = result.deviceLocation
//        val map = Arguments.createMap()
//        map.putDouble("latitude", deviceLocation.latitude)
//        map.putDouble("longitude", deviceLocation.longitude)
//        promise.resolve(map)
//      } else {
//        val error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL)
//        promise.reject(error)
//      }
//    } catch (e: Exception) {
//      val error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL)
//      promise.reject(error, e)
//    }
//  }

  private fun expectedLocationFromMap(expectedLocation: ReadableMap): Location? {
    return if (!expectedLocation.hasKey(LATITUDE) || !expectedLocation.hasKey(LONGITUDE)) null else try {
      val latitude = expectedLocation.getDouble(LATITUDE)
      val longitude = expectedLocation.getDouble(LONGITUDE)
      if (longitude > 180.0 || longitude < -180.0) return null
      if (latitude > 90.0 || latitude < -90.0) return null
      val expected = Location("any")
      expected.latitude = latitude
      expected.longitude = longitude
      expected
    } catch (ignored: Throwable) {
      null
    }
  }

  private fun fail(error: Int, promise: Promise) {
    val map = Arguments.createMap()
    map.putInt("code", error)
    promise.resolve(map)
  }

  @ReactMethod
  fun setDeviceName(name: String?, promise: Promise) {
    try {
      sdkInstance!!.setDeviceName(name)
      promise.resolve(true)
    } catch (e: Exception) {
      val error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL)
      promise.reject(error, e)
    }
  }

  @ReactMethod
  fun setMetadata(rMap: ReadableMap, promise: Promise) {
    try {
      sdkInstance!!.setDeviceMetadata(rMap.toHashMap())
      promise.resolve(true)
    } catch (e: Exception) {
      val error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL)
      promise.reject(error, e)
    }
  }

  @ReactMethod
  fun enableDebugLogging(enable: Boolean) {
    if (enable) {
      HyperTrack.enableDebugLogging()
    }
  }

  @ReactMethod
  fun enableMockLocation(enable: Boolean) {
    if (enable) {
      sdkInstance!!.allowMockLocations()
    }
  }

  @ReactMethod
  fun getLocation(promise: Promise) {
    promise.resolve(locationResultToMap(sdkInstance!!.latestLocation))
  }

  @ReactMethod
  fun getCurrentLocation(promise: Promise) {
    sdkInstance!!.getCurrentLocation { result -> promise.resolve(locationResultToMap(result)) }
  }

  private fun locationResultToMap(result: Result<Location, OutageReason>): WritableMap {
    val resultMap = Arguments.createMap()
    if (result.isSuccess) {
      val location = result.value
      val locationMap = Arguments.createMap()
      locationMap.putDouble(LATITUDE, location.latitude)
      locationMap.putDouble(LONGITUDE, location.longitude)
      resultMap.putMap(KEY_LOCATION, locationMap)
    } else {
      val outage = result.error
      val errorMap = Arguments.createMap()
      errorMap.putString(KEY_OUTAGE_CODE, getOutageName(outage))
      resultMap.putMap(KEY_ERROR, errorMap)
    }
    return resultMap
  }

  private fun getOutageName(outageReason: OutageReason): String {
    return when (outageReason) {
      OutageReason.MISSING_LOCATION_PERMISSION -> OUTAGE_CODE_LOCATION_PERMISSIONS_DENIED
      OutageReason.MISSING_ACTIVITY_PERMISSION -> OUTAGE_CODE_MOTION_ACTIVITY_PERMISSIONS_DENIED
      OutageReason.LOCATION_SERVICE_DISABLED -> OUTAGE_CODE_LOCATION_SERVICES_DISABLED
      OutageReason.NOT_TRACKING -> OUTAGE_CODE_NOT_RUNNING
      OutageReason.START_HAS_NOT_FINISHED -> OUTAGE_CODE_STARTING
      OutageReason.NO_GPS_SIGNAL, OutageReason.RESTART_REQUIRED -> OUTAGE_CODE_GPS_SIGNAL_LOST
      else -> OUTAGE_CODE_GPS_SIGNAL_LOST
    }
  }

  private fun convertToRNErrorsCode(androidSdkCode: Int): Int {
    return when (androidSdkCode) {
      TrackingError.INVALID_PUBLISHABLE_KEY_ERROR -> RN_ERROR_INVALID_PUBLISHABLE_KEY
      TrackingError.AUTHORIZATION_ERROR -> RN_ERROR_AUTHORIZATION_FAILED
      TrackingError.PERMISSION_DENIED_ERROR -> RN_ERROR_PERMISSION_DENIED
      else -> RN_ERROR_GENERAL
    }
  }

  companion object {
    private const val TAG = "HTSDKModule"
    private const val KEY_LOCATION = "location"
    private const val KEY_ERROR = "error"
    private const val KEY_OUTAGE_CODE = "code"
    const val NAME = "HyperTrack"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
    const val ACCURACY = "accuracy"
    private const val OUTAGE_CODE_LOCATION_PERMISSIONS_DENIED = "location_permissions_denied"
    private const val OUTAGE_CODE_LOCATION_SERVICES_DISABLED = "location_services_disabled"
    private const val OUTAGE_CODE_MOTION_ACTIVITY_PERMISSIONS_DENIED =
      "motion_activity_permissions_denied"
    private const val OUTAGE_CODE_NOT_RUNNING = "not_running"
    private const val OUTAGE_CODE_GPS_SIGNAL_LOST = "gps_signal_lost"
    private const val OUTAGE_CODE_STARTING = "starting"
    private const val RN_ERROR_GENERAL = 0
    private const val RN_ERROR_INVALID_PUBLISHABLE_KEY = 1
    private const val RN_ERROR_AUTHORIZATION_FAILED = 2
    private const val RN_ERROR_PERMISSION_DENIED = 3
  }
}
