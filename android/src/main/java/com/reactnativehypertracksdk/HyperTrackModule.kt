package com.reactnativehypertracksdk

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.hypertrack.sdk.*
import com.hypertrack.sdk.TrackingStateObserver.OnTrackingStateChangeListener
import com.hypertrack.sdk.AvailabilityStateObserver.OnAvailabilityStateChangeListener
import com.reactnativehypertracksdk.common.*

@Suppress("ComplexRedundantLet")
@ReactModule(name = HyperTrackModule.NAME)
class HyperTrackModule(reactContext: ReactApplicationContext?) :
  ReactContextBaseJavaModule(reactContext) {

  var trackingStateListener: OnTrackingStateChangeListener? = null
  var availabilityListener: OnAvailabilityStateChangeListener? = null

  override fun getName(): String {
    return NAME
  }

  private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap?) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(eventName, params)
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
    publishableKey: String,
    initParams: Map<String, Boolean>,
    promise: Promise
  ) {
    HyperTrackSdkWrapper.initialize(
      publishableKey,
      SdkInitParams.fromMap(initParams)
    ).let {
      if(it is Success) {
        it.success.let { hyperTrack ->
          initListeners(hyperTrack)
        }
        Success(Unit)
      } else {
        it
      }
    }.toPromise(promise)
  }

  private fun initListeners(sdkInstance: HyperTrack) {
    if (trackingStateListener != null) {
      sdkInstance.removeTrackingListener(trackingStateListener)
      trackingStateListener = null
    }
    trackingStateListener = object : OnTrackingStateChangeListener {
      override fun onError(trackingError: TrackingError) {
        emitEvent(
          EVENT_ERROR,
          serializeErrors(
            HyperTrackSdkWrapper.getTrackingErrors(trackingError)
          ).toWriteableArray()
        )
      }

      override fun onTrackingStart() {
        sendEvent(reactContext, EVENT_TRACKING, serializeIsTracking(true).toWritableMap())
      }

      override fun onTrackingStop() {
        sendEvent(reactContext, EVENT_TRACKING, serializeIsTracking(false).toWritableMap())
      }
    }.also {
      sdkInstance.addTrackingListener(it)
    }

    if (availabilityListener != null) {
      sdkInstance.removeAvailabilityListener(availabilityListener)
      availabilityListener = null
    }
    availabilityListener = object : AvailabilityStateObserver.OnAvailabilityStateChangeListener {
      override fun onError(p0: AvailabilityError) {
        // ignored, errors are handled by trackingStateListener
      }

      override fun onAvailable() {
        emitEvent(EVENT_AVAILABILITY, serializeAvailability(true).toWritableMap())
      }

      override fun onUnavailable() {
        emitEvent(EVENT_AVAILABILITY, serializeAvailability(false).toWritableMap())
      }
    }.also {
      sdkInstance.addAvailabilityListener(it)
    }
  }

  @ReactMethod
  fun getDeviceID(promise: Promise) {
    HyperTrackSdkWrapper.getDeviceID().toPromise(promise)
  }

  @ReactMethod
  fun isTracking(promise: Promise) {
    HyperTrackSdkWrapper.isTracking().toPromise(promise)
  }

  @ReactMethod
  fun startTracking() {
    HyperTrackSdkWrapper.startTracking()
  }

  @ReactMethod
  fun stopTracking() {
    HyperTrackSdkWrapper.stopTracking()
  }

  @ReactMethod
  fun syncDeviceSettings() {
    HyperTrackSdkWrapper.sync()
  }

  @ReactMethod
  fun addGeotag(rMap: ReadableMap, promise: Promise) {
    HyperTrackSdkWrapper.addGeotag(rMap.toHashMap())
  }

  @ReactMethod
  fun isAvailable(promise: Promise) {
    HyperTrackSdkWrapper.isAvailable().toPromise(promise)
  }

  @ReactMethod
  fun setAvailability(availability: Map<String, Boolean>) {
    HyperTrackSdkWrapper.setAvailability(availability)
  }

  @ReactMethod
  fun setDeviceName(name: String) {
    HyperTrackSdkWrapper.setName(name)
  }

  @ReactMethod
  fun setMetadata(rMap: ReadableMap) {
    HyperTrackSdkWrapper.setMetadata(rMap.toHashMap())
  }

  @ReactMethod
  fun getLocation(promise: Promise) {
    HyperTrackSdkWrapper.getLocation().toPromise(promise)
  }

  private fun emitEvent(event: String, data: WritableMap) {
    reactApplicationContext
      .getJSModule(RCTDeviceEventEmitter::class.java)
      .emit(event, data)
  }

  private fun emitEvent(event: String, data: WritableArray) {
    reactApplicationContext
      .getJSModule(RCTDeviceEventEmitter::class.java)
      .emit(event, data)
  }

  companion object {
    private const val EVENT_TRACKING = "onTrackingChanged"
    private const val EVENT_AVAILABILITY = "onAvailabilityChanged"
    private const val EVENT_ERROR = "onError"

    const val NAME = "HyperTrack"
  }
}
