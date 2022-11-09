package com.reactnativehypertracksdk

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.hypertrack.sdk.*
import com.hypertrack.sdk.TrackingStateObserver.OnTrackingStateChangeListener
import com.hypertrack.sdk.AvailabilityStateObserver.OnAvailabilityStateChangeListener
import com.reactnativehypertracksdk.common.*
import com.reactnativehypertracksdk.common.Serialization.serializeIsAvailable
import com.reactnativehypertracksdk.common.Serialization.serializeIsTracking

@Suppress("ComplexRedundantLet")
@ReactModule(name = HyperTrackModule.NAME)
class HyperTrackModule(reactContext: ReactApplicationContext?) :
    ReactContextBaseJavaModule(reactContext) {

    var trackingStateListener: OnTrackingStateChangeListener? = null
    var availabilityListener: OnAvailabilityStateChangeListener? = null

    override fun getName(): String {
        return NAME
    }

    @ReactMethod
    fun addListener(type: String?) {
        HyperTrackSdkWrapper.withSdkInstance {
            when(type) {
                EVENT_TRACKING -> {
                    HyperTrackSdkWrapper.isTracking().let {
                        when(it) {
                            is Success -> {
                                emitIsTracking(it.success)
                            }
                            is Failure -> {
                                throw Exception("isTracking failed: ${it.failure}", it.failure)
                            }
                        }
                    }
                }
                EVENT_AVAILABILITY -> {
                    HyperTrackSdkWrapper.isAvailable().let {
                        when(it) {
                            is Success -> {
                                emitIsAvailable(it.success)
                            }
                            is Failure -> {
                                throw Exception("isAvailable failed: ${it.failure}", it.failure)
                            }
                        }
                    }
                }
                EVENT_ERRORS -> {
                    HyperTrackSdkWrapper.getInitialErrors().let {
                        emitErrors(it)
                    }
                }
                else -> Unit
            }
        }
        // Keep: Required for RN built in Event Emitter Calls.
    }

    @ReactMethod
    fun removeListeners(type: Int?) {
        // Keep: Required for RN built in Event Emitter Calls.
    }

    @ReactMethod
    fun initialize(
        initParams: ReadableMap,
        promise: Promise
    ) {
        HyperTrackSdkWrapper
            .initialize(initParams.toHashMap())
            .mapSuccess { sdk ->
                initListeners(sdk)
            }
            .toPromise(promise)
    }

    private fun initListeners(sdkInstance: HyperTrack) {
        if (trackingStateListener != null) {
            sdkInstance.removeTrackingListener(trackingStateListener)
            trackingStateListener = null
        }
        trackingStateListener = object : OnTrackingStateChangeListener {
            override fun onError(trackingError: TrackingError) {
                emitErrors(HyperTrackSdkWrapper.getErrors(trackingError))
            }

            override fun onTrackingStart() {
                emitIsTracking(serializeIsTracking(true))
            }

            override fun onTrackingStop() {
                emitIsTracking(serializeIsTracking(false))
            }
        }.also {
            sdkInstance.addTrackingListener(it)
        }

        if (availabilityListener != null) {
            sdkInstance.removeAvailabilityListener(availabilityListener)
            availabilityListener = null
        }
        availabilityListener =
            object : AvailabilityStateObserver.OnAvailabilityStateChangeListener {
                override fun onError(p0: AvailabilityError) {
                    // ignored, errors are handled by trackingStateListener
                }

                override fun onAvailable() {
                    emitIsAvailable(serializeIsAvailable(true))
                }

                override fun onUnavailable() {
                    emitIsAvailable(serializeIsAvailable(false))
                }
            }.also {
                sdkInstance.addAvailabilityListener(it)
            }
    }

    @ReactMethod
    fun getDeviceId(promise: Promise) {
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
    fun addGeotag(args: ReadableMap, promise: Promise) {
        HyperTrackSdkWrapper.addGeotag(args.toHashMap()).toPromise(promise)
    }

    @ReactMethod
    fun isAvailable(promise: Promise) {
        HyperTrackSdkWrapper.isAvailable().toPromise(promise)
    }

    @ReactMethod
    fun setAvailability(args: ReadableMap) {
        HyperTrackSdkWrapper.setAvailability(args.toHashMap())
    }

    @ReactMethod
    fun setDeviceName(args: ReadableMap) {
        HyperTrackSdkWrapper.setName(args.toHashMap())
    }

    @ReactMethod
    fun setMetadata(args: ReadableMap) {
        HyperTrackSdkWrapper.setMetadata(args.toHashMap())
    }

    @ReactMethod
    fun getLocation(promise: Promise) {
        HyperTrackSdkWrapper.getLocation().toPromise(promise)
    }

    private fun emitIsTracking(isTracking: Map<String, Any?>) {
        emitEvent(EVENT_TRACKING, isTracking.toWritableMap())
    }

    private fun emitIsAvailable(isAvailable: Map<String, Any?>) {
        emitEvent(EVENT_AVAILABILITY, isAvailable.toWritableMap())
    }

    private fun emitErrors(errors: List<Map<String, Any?>>) {
        emitEvent(EVENT_ERRORS, errors.toWriteableArray())
    }

    private fun emitEvent(event: String, data: WritableMap) {
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(event, data)
    }

    private fun emitEvent(event: String, data: WritableArray) {
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(event, data)
    }

    companion object {
        private const val EVENT_TRACKING = "onTrackingChanged"
        private const val EVENT_AVAILABILITY = "onAvailabilityChanged"
        private const val EVENT_ERRORS = "onError"

        const val NAME = "HyperTrack"
    }
}
