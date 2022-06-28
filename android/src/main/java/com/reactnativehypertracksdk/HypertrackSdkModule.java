package com.reactnativehypertracksdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hypertrack.sdk.HyperTrack;

import java.util.Locale;

@ReactModule(name = HyperTrackSdkModule.NAME)
public class HyperTrackSdkModule extends ReactContextBaseJavaModule {
  public static final String NAME = "HyperTrackSdk";
  private int eventCount = 0;
  public HyperTrack sdkInstance;
  public HyperTrackSdkModule(ReactApplicationContext context) {
    super(context);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  private void sendEvent(ReactContext reactContext,
      String eventName,
      int params) {
    reactContext
        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
        .emit(eventName, params);
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(int a, int b, Promise promise) {
    promise.resolve(a * b);
  }

  public static native int nativeMultiply(int a, int b);

  public static String getMetadata(Context context, String key) {
    try {
      Bundle metaData = context.getPackageManager()
          .getApplicationInfo(context.getPackageName(),
              PackageManager.GET_META_DATA).metaData;
      return metaData.getString(key);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getPackageName(Context context) {
    try {
      return context.getPackageManager().getPackageInfo(
          context.getPackageName(), 0).packageName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  @ReactMethod
  public void createCalendarEvent() {
    Log.d("Hyper", getMetadata(getReactApplicationContext(), "HyperTrackPublishableKey"));
    Log.d("Hyper", getPackageName(getReactApplicationContext()));
  }

  @ReactMethod
  public void createCalendarPromise(Promise promise) {
    try {
      promise.resolve("data returned from promise");
      eventCount += 1;
      sendEvent(getReactApplicationContext(), "Event count", eventCount);
    } catch (Exception e) {
      promise.reject("error returned from promise", e);
    }
  }

  @ReactMethod
  public void initialize(Boolean startsTracking, Boolean automaticallyRequestPermissions, Promise promise) {
    try {
      sdkInstance = HyperTrack.getInstance(
        getMetadata(getReactApplicationContext(), "HyperTrackPublishableKey")
      ).backgroundTrackingRequirement(false);
      if (startsTracking) {
        sdkInstance.start();
      }
      promise.resolve(true);
    } catch (java.lang.Exception exception) {
      Log.e("Hyper", "Hypertrack SDK initialization failed.", exception);
      String error = String.format(Locale.ENGLISH, "%d", "invalid publishable key");
      promise.reject(error, new Exception("Hypertrack SDK initialization failed."));
    }
  }

  @ReactMethod
  public void getDeviceID(Promise promise) {
    promise.resolve(sdkInstance.getDeviceID());
  }
}
