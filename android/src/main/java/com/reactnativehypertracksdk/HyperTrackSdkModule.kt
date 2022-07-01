package com.reactnativehypertracksdk

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.hypertrack.sdk.HyperTrack
import java.util.*


class HypertrackSdkModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return "HyperTrackSdk"
  }

  private var eventCount = 0
  var sdkInstance: HyperTrack? = null

  private fun sendEvent(
    reactContext: ReactContext,
    eventName: String,
    params: Int
  ) {
    reactContext
      .getJSModule(RCTDeviceEventEmitter::class.java)
      .emit(eventName, params)
  }

  fun getMetadata(context: Context, key: String?): String? {
    try {
      val metaData = context.packageManager
        .getApplicationInfo(
          context.packageName,
          PackageManager.GET_META_DATA
        ).metaData
      return metaData.getString(key)
    } catch (e: NameNotFoundException) {
      e.printStackTrace()
    }
    return null
  }

  fun getPackageName(context: Context): String? {
    try {
      return context.packageManager.getPackageInfo(
        context.packageName, 0
      ).packageName
    } catch (e: NameNotFoundException) {
      e.printStackTrace()
    }
    return null
  }

  @ReactMethod
  fun createCalendarEvent() {
    Log.d("Hyper", getMetadata(reactApplicationContext, "HyperTrackPublishableKey")!!)
    Log.d("Hyper", getPackageName(reactApplicationContext)!!)
  }

  @ReactMethod
  fun createCalendarPromise(promise: Promise) {
    try {
      promise.resolve("data returned from promise")
      eventCount += 1
      sendEvent(reactApplicationContext, "Event count", eventCount)
    } catch (e: Exception) {
      promise.reject("error returned from promise", e)
    }
  }

  @ReactMethod
  fun initialize(
    startsTracking: Boolean,
    automaticallyRequestPermissions: Boolean?,
    promise: Promise
  ) {
    try {
      sdkInstance = HyperTrack.getInstance(
        getMetadata(reactApplicationContext, "HyperTrackPublishableKey")!!
      ).backgroundTrackingRequirement(false)
      if (startsTracking) {
        sdkInstance?.start()
      }
      promise.resolve(true)
    } catch (exception: Exception) {
      Log.e("Hyper", "Hypertrack SDK initialization failed.", exception)
      val error = String.format(Locale.ENGLISH, "%d", "invalid publishable key")
      promise.reject(error, Exception("Hypertrack SDK initialization failed."))
    }
  }

  @ReactMethod
  fun getDeviceID(promise: Promise) {
    promise.resolve(sdkInstance!!.deviceID)
  }
}
