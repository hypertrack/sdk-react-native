package io.hypertrack;
import com.hypertrack.sdk.TrackingInitDelegate;
import com.hypertrack.sdk.TrackingInitError;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.facebook.react.bridge.Promise;
import java.util.Map;
import com.hypertrack.sdk.HyperTrack;
import com.hypertrack.sdk.logger.HTLogger;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RNHyperTrackModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

 private static final String TAG = RNHyperTrackModule.class.getSimpleName();
 private final ReactApplicationContext reactContext;

 public RNHyperTrackModule(ReactApplicationContext reactContext) {
  super(reactContext);
  this.reactContext = reactContext;
  reactContext.addLifecycleEventListener(this);
 }

 @Override
 public String getName() {
  return "RNHyperTrack";
 }

 @ReactMethod
 public void initialize(String publishableKey, Boolean startsTracking) {
  TrackingInitDelegate delegate = new TrackingInitDelegate() {
            @Override public void onError(@NonNull TrackingInitError error) { }
            @Override public void onSuccess() { }
        };

  HyperTrack.initialize(getReactApplicationContext().getCurrentActivity(), publishableKey, startsTracking, startsTracking, delegate);
 }

 @Override
 public void onHostDestroy() {
  Log.d(TAG, "onHostDestroy: ");
 }


 @Override
 public void onHostPause() {
  Log.d(TAG, "onHostPause: ");
 }

 @Override
 public void onHostResume() {
  Log.d(TAG, "onHostResume: ");
  //registerBroadcastReceiver();
 }

 @ReactMethod
 public void getDeviceID(Promise promise) {
  promise.resolve(HyperTrack.getDeviceId());
 }

 @ReactMethod
 public void isTracking(Promise promise) {
  promise.resolve(HyperTrack.isTracking());
 }


 @ReactMethod
 public void startTracking() {
  HyperTrack.startTracking(true, null);
 }

 @ReactMethod
 public void stopTracking() {
  HyperTrack.stopTracking();
 }

 @ReactMethod
 public void setTripMarker(ReadableMap rMap) {
  HashMap < String, Object > map = recursivelyDeconstructReadableMap(rMap);
  HyperTrack.tripMarker(map);
 }

 @ReactMethod
 public void setDevice(String name, ReadableMap rMap) {
  HashMap < String, Object > map = recursivelyDeconstructReadableMap(rMap);
  HyperTrack.setNameAndMetadataForDevice(name, map);
 }

 private HashMap < String, Object > recursivelyDeconstructReadableMap(ReadableMap readableMap) {
  ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
  HashMap < String, Object > deconstructedMap = new HashMap < > ();
  while (iterator.hasNextKey()) {
   String key = iterator.nextKey();
   ReadableType type = readableMap.getType(key);
   switch (type) {
    case Null:
     deconstructedMap.put(key, null);
     break;
    case Boolean:
     deconstructedMap.put(key, readableMap.getBoolean(key));
     break;
    case Number:
     deconstructedMap.put(key, readableMap.getDouble(key));
     break;
    case String:
     deconstructedMap.put(key, readableMap.getString(key));
     break;
    case Map:
     deconstructedMap.put(key, recursivelyDeconstructReadableMap(readableMap.getMap(key)));
     break;
    case Array:
     deconstructedMap.put(key, recursivelyDeconstructReadableArray(readableMap.getArray(key)));
     break;
    default:
     throw new IllegalArgumentException("Could not convert object with key: " + key + ".");
   }

  }
  return deconstructedMap;
 }

 private List < Object > recursivelyDeconstructReadableArray(ReadableArray readableArray) {
  List < Object > deconstructedList = new ArrayList < > (readableArray.size());
  for (int i = 0; i < readableArray.size(); i++) {
   ReadableType indexType = readableArray.getType(i);
   switch (indexType) {
    case Null:
     deconstructedList.add(i, null);
     break;
    case Boolean:
     deconstructedList.add(i, readableArray.getBoolean(i));
     break;
    case Number:
     deconstructedList.add(i, readableArray.getDouble(i));
     break;
    case String:
     deconstructedList.add(i, readableArray.getString(i));
     break;
    case Map:
     deconstructedList.add(i, recursivelyDeconstructReadableMap(readableArray.getMap(i)));
     break;
    case Array:
     deconstructedList.add(i, recursivelyDeconstructReadableArray(readableArray.getArray(i)));
     break;
    default:
     throw new IllegalArgumentException("Could not convert object at index " + i + ".");
   }
  }
  return deconstructedList;
 }
}
