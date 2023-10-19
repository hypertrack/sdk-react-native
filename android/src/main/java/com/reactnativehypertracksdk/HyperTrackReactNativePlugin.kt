package com.reactnativehypertracksdk

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.hypertrack.sdk.*
import com.hypertrack.sdk.android.HyperTrack
import com.reactnativehypertracksdk.common.*
import com.reactnativehypertracksdk.common.Serialization.serializeErrors
import com.reactnativehypertracksdk.common.Serialization.serializeIsAvailable
import com.reactnativehypertracksdk.common.Serialization.serializeIsTracking
import com.reactnativehypertracksdk.common.Serialization.serializeLocateResult
import com.reactnativehypertracksdk.common.Serialization.serializeLocation
import com.reactnativehypertracksdk.common.Serialization.serializeLocationResult

@ReactModule(name = HyperTrackReactNativePlugin.NAME)
class HyperTrackReactNativePlugin(reactContext: ReactApplicationContext?) :
    ReactContextBaseJavaModule(reactContext) {

    private var locateSubscription: HyperTrack.Cancellable? = null
    private var subscriptions: List<HyperTrack.Cancellable>? = null

    override fun getName(): String {
        return NAME
    }

    /**
     * ReactNative built-in methods
     */

    @ReactMethod
    fun addListener(eventName: String?) {
        // called when RN app subscribes to an event
        /**
         * We init listeners lazily to avoid calling any SDK method before dynamic publishable key
         * is set.
         */
        if(subscriptions == null) {
            subscriptions = initListeners()
        }
        when (eventName) {
            EVENT_ERRORS -> {
                emitEvent(EVENT_ERRORS, serializeErrors(HyperTrack.errors).toWriteableArray())
            }
            EVENT_IS_AVAILABLE -> {
                emitEvent(EVENT_IS_AVAILABLE, serializeIsAvailable(HyperTrack.isAvailable).toWritableMap())
            }
            EVENT_IS_TRACKING -> {
                emitEvent(EVENT_IS_TRACKING, serializeIsTracking(HyperTrack.isTracking).toWritableMap())
            }
            EVENT_LOCATION -> {
                emitEvent(EVENT_LOCATION, serializeLocationResult(HyperTrack.location).toWritableMap())
            }
            else -> Unit
        }
        // Keep: Required for RN built in Event Emitter Calls.
    }

    @ReactMethod
    fun removeListeners(type: Int?) {
        // Keep: Required for RN built in Event Emitter Calls.
    }

    /**
     * SDK methods
     */

    @ReactMethod
    fun addGeotag(args: ReadableMap, promise: Promise) {
        HyperTrackSdkWrapper.addGeotag(args.toHashMap()).toPromise(promise)
    }

    @ReactMethod
    fun getDeviceId(promise: Promise) {
        HyperTrackSdkWrapper.getDeviceId().toPromise(promise)
    }

    @ReactMethod
    fun getErrors(promise: Promise) {
        HyperTrackSdkWrapper.getErrors().toPromise(promise)
    }

    @ReactMethod
    fun getIsAvailable(promise: Promise) {
        HyperTrackSdkWrapper.getIsAvailable().toPromise(promise)
    }

    @ReactMethod
    fun getIsTracking(promise: Promise) {
        HyperTrackSdkWrapper.getIsTracking().toPromise(promise)
    }

    @ReactMethod
    fun getLocation(promise: Promise) {
        HyperTrackSdkWrapper.getLocation().toPromise(promise)
    }

    @ReactMethod
    fun getMetadata(promise: Promise) {
        HyperTrackSdkWrapper.getMetadata().toPromise(promise)
    }

    @ReactMethod
    fun getName(promise: Promise) {
        HyperTrackSdkWrapper.getName().toPromise(promise)
    }

    @ReactMethod
    fun locate(promise: Promise) {
        locateSubscription?.cancel()
        locateSubscription = HyperTrack.locate {
            emitEvent(EVENT_LOCATE, serializeLocateResult(it).toWritableMap())
        }
        Success(Unit).toPromise(promise)
    }

    @ReactMethod
    fun setIsAvailable(args: ReadableMap) {
        HyperTrackSdkWrapper.setIsAvailable(args.toHashMap())
    }

    @ReactMethod
    fun setIsTracking(args: ReadableMap) {
        HyperTrackSdkWrapper.setIsTracking(args.toHashMap())
    }

    @ReactMethod
    fun setMetadata(args: ReadableMap) {
        HyperTrackSdkWrapper.setMetadata(args.toHashMap())
    }

    @ReactMethod
    fun setName(args: ReadableMap) {
        HyperTrackSdkWrapper.setName(args.toHashMap())
    }

    private fun initListeners(): List<HyperTrack.Cancellable> {
        return mutableListOf<HyperTrack.Cancellable>().also { result ->
            HyperTrack.subscribeToErrors {
                emitEvent(EVENT_ERRORS, serializeErrors(it).toWriteableArray())
            }.also {
                result.add(it)
            }

            HyperTrack.subscribeToIsAvailable {
                emitEvent(EVENT_IS_AVAILABLE, serializeIsAvailable(it).toWritableMap())
            }.also {
                result.add(it)
            }

            HyperTrack.subscribeToIsTracking {
                emitEvent(EVENT_IS_TRACKING, serializeIsTracking(it).toWritableMap())
            }.also {
                result.add(it)
            }

            HyperTrack.subscribeToLocation {
                emitEvent(EVENT_LOCATION, serializeLocationResult(it).toWritableMap())
            }.also {
                result.add(it)
            }
        }
    }

    private fun emitEvent(event: String, data: WritableMap) {
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(event, data)
    }

    @Suppress("SameParameterValue")
    private fun emitEvent(event: String, data: WritableArray) {
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(event, data)
    }

    companion object {
        private const val EVENT_ERRORS = "errors"
        private const val EVENT_IS_AVAILABLE = "isAvailable"
        private const val EVENT_IS_TRACKING = "isTracking"
        private const val EVENT_LOCATE = "locate"
        private const val EVENT_LOCATION = "location"

        const val NAME = "HyperTrackReactNativePlugin"
    }
}
